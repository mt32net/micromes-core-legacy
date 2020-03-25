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
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import io.ktor.util.hex
import net.micromes.db.dBConnect
import net.micromes.db.dBInit
import net.micromes.entities.GoogleAccount
import net.micromes.entities.User
import net.micromes.google.OAuthClient
import net.micromes.graphql.Context
import net.micromes.graphql.Mutation
import net.micromes.graphql.Query

data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String>
)

var googleOAuth:OAuthClient = OAuthClient()

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
            post("/") {
                val rawBody = call.receive<String>()
                val reqBody : Body = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).readValue(rawBody)
                //val reqBody = call.receive<Map<String, String>>()
                println(reqBody.variables)
                val execBuilder = ExecutionInput.newExecutionInput()
                    .query(reqBody.query)
                    .operationName(reqBody.operationName)
                    .variables(reqBody.variables)
                    .context(Context(call.sessions.get<GoogleAccount>() ?: throw RuntimeException("Not logged in yet!")))
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
