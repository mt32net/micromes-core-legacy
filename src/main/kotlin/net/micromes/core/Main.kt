package net.micromes.core

import net.micromes.core.auth.loadKey
import com.expediagroup.graphql.SchemaGeneratorConfig
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.toSchema
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import net.micromes.core.auth.getUserForToken
import net.micromes.core.db.DBUser
import net.micromes.core.db.dBConnect
import net.micromes.core.db.dBInit
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.QueryException
import net.micromes.core.exceptions.SimpleHTTPException
import net.micromes.core.exceptions.UserNotFound
import net.micromes.core.graphql.Context
import net.micromes.core.graphql.Mutation
import net.micromes.core.graphql.Query


data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String>
)

val publicKey = loadKey()

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
            get("/getUserByID") {
                try {
                    val user: User = DBUser().getUserByID(call.request.queryParameters["id"]?.toLong() ?: throw SimpleHTTPException(400)) ?: throw UserNotFound()
                    val externalUser = ExternalUser(user)
                    call.respond(externalUser)
                } catch (e: QueryException) {
                    call.respond(HttpStatusCode(e.getRCode(), e.message ?: "Bad Request"))
                } catch (e: SimpleHTTPException) {
                    call.respond(HttpStatusCode(e.rCode, "Bad Request"))
                }
            }
            post("/api") {

                val token: String = call.request.headers["Authorization"]?.substring(7)
                    ?: throw RuntimeException("No authentication header")

                val dbUser = DBUser()
                val responseBodyOnError = object {
                    val errors = mutableListOf<QueryException>()
                }
                try {
                    val rawBody = call.receive<String>()
                    val reqBody : Body = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).readValue(rawBody)

                    val user: User = getUserForToken(token, publicKey)

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
