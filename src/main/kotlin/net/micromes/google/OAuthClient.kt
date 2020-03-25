package net.micromes.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.LowLevelHttpRequest
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.JsonGenerator
import com.google.api.client.json.JsonParser
import com.google.api.client.json.jackson2.JacksonFactory
import net.micromes.config.Settings
import net.micromes.entities.GoogleAccount
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import java.nio.charset.Charset
import java.util.*

class OAuthClient{
    lateinit var transport: HttpTransport
    var jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
    val clientId = Settings.clientId
    val clientSecret = Settings.clientSecret
    lateinit var verifier: GoogleIdTokenVerifier

    init{
        transport = GoogleNetHttpTransport.newTrustedTransport()
        verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList((clientId)))
            .build()
    }

    fun authentificate(idTokenString: String): GoogleAccount {
        val idToken: GoogleIdToken? = verifier.verify(idTokenString)
        if (idToken != null) {
            val payload: GoogleIdToken.Payload = idToken.payload

            // Print user identifier
            val userId: String = payload.subject
            println("User ID: $userId")

            // Get profile information from payload
            val email: String = payload.email
            val emailVerified: Boolean = java.lang.Boolean.valueOf(payload.emailVerified)
            val name = payload["name"] as String
            val pictureUrl = payload["picture"] as String
            val locale = payload["locale"] as String
            val familyName = payload["family_name"] as String
            val givenName = payload["given_name"] as String

            return GoogleAccount(idToken.toString(), email, emailVerified,
                name, pictureUrl, locale, familyName, givenName)
        } else {
            println("Invalid ID token.")
            return GoogleAccount("", "", false, "",
                "","","","")
        }
    }
}