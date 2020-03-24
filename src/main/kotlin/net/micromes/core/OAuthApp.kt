package net.micromes.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.origin
import io.ktor.http.HttpMethod
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import io.ktor.util.hex

val mciromesgoogleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "google",
    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
    accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
    requestMethod = HttpMethod.Post,

    clientId = "1025113353398-pb40di8kma99osibf68j8ov8fqvddr96.apps.googleusercontent.com", // @TODO: Remember to change this!
    clientSecret = "ZtS_4ANT1xX3SPlNgPIMjNzW", // @TODO: Remember to change this!
    defaultScopes = listOf("profile") // no email, but gives full name, picture, and id
)

class MySession(val userId: String)

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8090) {
        install(Sessions) {
            cookie<MySession>("oauthSampleSessionId") {
                val secretSignKey = hex("000102030405060708090a0b0c0d0e0f") // @TODO: Remember to change this!
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(Authentication) {
            oauth("google-oauth") {
                client = HttpClient(Apache)
                providerLookup = { mciromesgoogleOauthProvider }
                urlProvider = {
                    redirectUrl("/login")
                }
            }
        }
        routing {
            get("/") {
                val session = call.sessions.get<MySession>()
                call.respondText("HI ${session?.userId}")
            }
            authenticate("google-oauth") {
                route("/login") {
                    handle {
                        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                            ?: error("No principal")

                        val json = HttpClient(Apache).get<String>("https://www.googleapis.com/userinfo/v2/me") {
                            header("Authorization", "Bearer ${principal.accessToken}")
                        }

                        val data : Map<String, Any?> = ObjectMapper().readValue(json)
                        val id = data["id"] as String?

                        if (id != null) {
                            call.sessions.set(MySession(id))
                        }
                        call.respondRedirect("/")
                    }
                }
            }
        }
    }.start(wait = true)
}

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}

