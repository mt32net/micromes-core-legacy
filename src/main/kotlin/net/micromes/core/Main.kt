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
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.routing.get
import io.ktor.routing.post

data class Body(
    val query : String,
    val operationName: String?,
    val variables: Map<String, String>
)

fun main() {
    val gql = (GraphQL.newGraphQL(getSchema()) ?: return).build()
    embeddedServer(Netty, 8090) {
        routing {
            install(ContentNegotiation) {
                jackson {
                }
            }
            get("/") {
                call.respondText { "Hallo!" }
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
                    .context("Hallo")
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