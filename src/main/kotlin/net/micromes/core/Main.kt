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
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import net.micromes.core.db.createNewUserAndReturn
import net.micromes.core.db.dBConnect
import net.micromes.core.db.dBInit
import net.micromes.core.db.getUserByExternalID
import net.micromes.core.entities.GoogleAccount
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import net.micromes.core.exceptions.NotAuthenticatedException
import net.micromes.core.exceptions.QueryException
import net.micromes.core.google.OAuthClient
import net.micromes.core.graphql.Context
import net.micromes.core.graphql.Mutation
import net.micromes.core.graphql.Query
import java.net.URI
import java.util.*

data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String>
)

var oauthClient: OAuthClient = OAuthClient()

fun main() {
    //mysql
    dBConnect()
    dBInit()

    val gql = (GraphQL.newGraphQL(getSchema()) ?: return).build()
    embeddedServer(Netty, 8090) {
        install(ContentNegotiation) {
            jackson {
            }
        }
        routing {
            get("/") {
                call.respondText { "Hello" }
            }
            post("/api") {
                val responseBodyOnError = object {
                    val errors = mutableListOf<QueryException>()
                }
                try {
                    val authHeader = call.request.headers["Authorization"]
                    val idToken: String = authHeader?.substring(7) ?: throw NotAuthenticatedException("no authentication header")
                    val account : GoogleAccount = oauthClient.authenticate(idToken)
                    val user : User = getUserByExternalID(account.id) ?: createNewUserAndReturn(UserImpl(
                        uuid = UUID.randomUUID(),
                        name = account.givenName,
                        status = Status.ONLINE,
                        profilePictureLocation = URI.create(account.pictureURl)
                    ), googleID = account.id)
                    val rawBody = call.receive<String>()
                    val reqBody : Body = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).readValue(rawBody)
                    //val reqBody = call.receive<Map<String, String>>()
                    //println(reqBody.variables)
                    val execBuilder = ExecutionInput.newExecutionInput()
                        .query(reqBody.query)
                        .operationName(reqBody.operationName)
                        .variables(reqBody.variables)
                        .context(Context(user))
                    val executionResult = gql.execute(execBuilder.build())
                    if (executionResult.errors.isNotEmpty()) println(executionResult.errors[0].message)
                    call.respond(executionResult)
                } catch (e: QueryException) {
                    e.printStackTrace()
                    responseBodyOnError.errors.add(e)
                    call.respond(responseBodyOnError)
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
