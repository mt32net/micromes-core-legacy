package net.micromes.core

import com.expediagroup.graphql.SchemaGeneratorConfig
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.toSchema
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.ContentNegotiation
import io.ktor.features.origin
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.sessions.*
import io.ktor.util.hex
import net.micromes.entities.GoogleAccount
import net.micromes.entities.User
import net.micromes.graphql.Context
import net.micromes.graphql.Mutation
import net.micromes.graphql.Query
import java.net.URI

data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String>
)

val miciromesgoogleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "google",
    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
    accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
    requestMethod = HttpMethod.Post,

    clientId = "1025113353398-pb40di8kma99osibf68j8ov8fqvddr96.apps.googleusercontent.com",
    clientSecret = "ZtS_4ANT1xX3SPlNgPIMjNzW",
    defaultScopes = listOf("profile")
)

fun main() {
    val gql = (GraphQL.newGraphQL(getSchema()) ?: return).build()
    embeddedServer(Netty, 8090) {
        install(Sessions) {
            //init cookie?
            cookie<GoogleAccount>("oauthSampleSessionId") {
                val secretSignKey = hex("000102030405060708090a0b0c0d0e0f") // @TODO: Remember to change this!
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(ContentNegotiation) {
            jackson {
            }
        }
        install(Authentication) {
            //redirect to google login when typing .../login in the browser
            oauth("google-oauth") {
                client = HttpClient(Apache)
                providerLookup = { miciromesgoogleOauthProvider }
                urlProvider = { redirectUrl("/login") }
            }
        }
        routing {
            get("/") {
                val session = call.sessions.get<GoogleAccount>()
                //get data from cookie and displaying data on site
                call.respondText { "Hello ${session?.name}" }
            }
            post("/") {
                val rawBody = call.receive<String>()
                val reqBody: Body =
                    jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                        .readValue(rawBody)
                //val reqBody = call.receive<Map<String, String>>()
                println(reqBody.variables)
                val execBuilder = ExecutionInput.newExecutionInput()
                    .query(reqBody.query)
                    .operationName(reqBody.operationName)
                    .variables(reqBody.variables)
                    .context(Context(User(username = "Matti")))
                val executionResult = gql.execute(execBuilder.build())
                if (executionResult.errors.isNotEmpty()) println(executionResult.errors[0].message)
                call.respond(executionResult)
            }
            authenticate("google-oauth") {
                route("/login") {
                    handle {
                        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                            ?: error("No principal")

                        //get user info
                        val json = HttpClient(Apache).get<String>("https://www.googleapis.com/userinfo/v2/me") {
                            header("Authorization", "Bearer ${principal.accessToken}")
                        }

                        //user info json to map
                        val data: Map<String, Any?> = ObjectMapper().readValue(json)
                        val id = data["id"] as String?
                        val name = data["name"] as String?
                        val pictureLink = data["picture"] as String?
                        val locale = data["locale"] as String?

                        //save user data in the cookie?
                        if (id != null && name != null && pictureLink != null && locale != null) {
                            call.sessions.set(GoogleAccount(id, name, pictureLink, locale))
                        }
                        println(data.toString())
                        call.respondRedirect("/")
                    }
                }
            }
        }
    }.start(true)
}


fun getSchema() : GraphQLSchema {
    val config = SchemaGeneratorConfig(listOf("net.micromes"))
    return toSchema(
        config = config,
        queries = listOf(TopLevelObject(Query())),
        mutations = listOf(TopLevelObject(Mutation()))
    )
}

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    var hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    hostPort = hostPort.substring(0, hostPort.length-2) + "80"
    return "$protocol://$hostPort$path"
}