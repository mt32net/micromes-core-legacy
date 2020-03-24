package net.micromes.core

import com.expediagroup.graphql.SchemaGeneratorConfig
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.toSchema
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.features.origin
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import net.micromes.db.connect
import net.micromes.entities.User
import net.micromes.graphql.Context
import net.micromes.graphql.Mutation
import net.micromes.graphql.Query
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String>
)

val googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "google",
    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
    accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
    requestMethod = HttpMethod.Post,

    clientId = "1025113353398-pb40di8kma99osibf68j8ov8fqvddr96.apps.googleusercontent.com",
    clientSecret = "ZtS_4ANT1xX3SPlNgPIMjNzW",
    defaultScopes = listOf("profile") // no email, but gives full name, picture, and id
)

fun main() {

    //mysql
    connect()
    transaction {
        addLogger(StdOutSqlLogger)
    }

    val gql = (GraphQL.newGraphQL(getSchema()) ?: return).build()
    embeddedServer(Netty, 8090) {
        routing {
            install(ContentNegotiation) {
                jackson {
                }
            }
            install(Authentication) {
                oauth("google-oauth") {
                    client = HttpClient(Apache)
                    providerLookup = { googleOauthProvider }
                    urlProvider = { redirectUrl("/login") }
                }
            }
            get("/") {
                call.respondText { "Hallo!" }
            }
            post("/") {
                val rawBody = call.receive<String>()
                val reqBody : Body = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).readValue(rawBody)
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
    val hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}