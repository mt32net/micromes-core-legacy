package net.micromes.core

import com.expediagroup.graphql.SchemaGeneratorConfig
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.toSchema
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.webSocket
import net.micromes.core.auth.getTokenPayload
import net.micromes.core.auth.loadKey
import net.micromes.core.config.Settings
import net.micromes.core.db.DBUser
import net.micromes.core.db.dBConnect
import net.micromes.core.db.dBInit
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import net.micromes.core.exceptions.InvalidTokenPayload
import net.micromes.core.exceptions.NoValidAuthHeader
import net.micromes.core.exceptions.QueryException
import net.micromes.core.graphql.Context
import net.micromes.core.graphql.Mutation
import net.micromes.core.graphql.Query


data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String> = mapOf()
)

val publicKey = loadKey()

fun main() {
    //mysql
    dBConnect()
    dBInit()

    val gql = (GraphQL.newGraphQL(getSchema()) ?: return).build()
    embeddedServer(Netty, 8090) {
        install(ContentNegotiation) {
            jackson()
        }
        routing {
            post("/registerUser") {
                val token: String = call.request.headers["Authorization"]?.substring(7)
                    ?: throw RuntimeException("No authentication header")
                try {
                    val payload = getTokenPayload(token, publicKey)
                    if (!payload.newUser) throw InvalidTokenPayload()
                    DBUser().createNewUser(
                        payload.sub.toLong(),
                        payload.newName ?: throw InvalidTokenPayload(),
                        Settings.DEFAULT_LOGO_URL.toASCIIString()
                    )
                    call.respond(HttpStatusCode.Created)
                } catch (e: QueryException) {
                    e.printStackTrace()
                    call.respond(e.message ?: "error")
                }
            }
            post("/api") {
                val responseBodyOnError = object {
                    val errors = mutableListOf<QueryException>()
                }
                try {
                    val token: String = call.request.headers["Authorization"]?.substring(7)
                        ?: throw NoValidAuthHeader()

                    val rawBody = call.receive<String>()
                    val reqBody : Body = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).readValue(rawBody)

                    val payload = getTokenPayload(token, publicKey)
                    if (payload.newUser) {
                        throw InvalidTokenPayload()
                    }
                    val user =  UserImpl(id = ID(payload.sub.toLong()))
                    val execBuilder = ExecutionInput.newExecutionInput()
                        .query(reqBody.query)
                        .operationName(reqBody.operationName)
                        .variables(reqBody.variables)
                        .context(Context(user))
                    val executionResult: ExecutionResult = gql.execute(execBuilder.build())
                    if (executionResult.errors.isNotEmpty()) println(executionResult.errors[0].message)
                    call.respond(executionResult.toSpecification())
                } catch (e: QueryException) {
                    e.printStackTrace()
                    responseBodyOnError.errors.add(e)
                    call.respond(HttpStatusCode.fromValue(e.getRCode()), responseBodyOnError)
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