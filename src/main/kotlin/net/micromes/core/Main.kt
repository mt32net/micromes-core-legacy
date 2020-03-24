package net.micromes.core

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.routing.get;

fun main(args: Array<String>) {
    embeddedServer(Netty, 8090) {
        routing {
            get("/") {
                call.respondText { "Hallo!" }
            }
        }
    }.start(true)
}