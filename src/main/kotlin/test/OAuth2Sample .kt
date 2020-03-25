package test/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.model.Tokeninfo
import com.google.api.services.oauth2.model.Userinfoplus
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * Command-line sample for the Google OAuth2 API described at [Using OAuth 2.0 for Login
 * (Experimental)](http://code.google.com/apis/accounts/docs/OAuth2Login.html).
 *
 * @author Yaniv Inbar
 */
object OAuth2Sample {
    /**
     * Be sure to specify the name of your application. If the application name is `null` or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private const val APPLICATION_NAME = ""

    /** Directory to store user credentials.  */
    private val DATA_STORE_DIR =
        File(System.getProperty("user.home"), ".store/oauth2_sample")

    /**
     * Global instance of the [DataStoreFactory]. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private var dataStoreFactory: FileDataStoreFactory? = null

    /** Global instance of the HTTP transport.  */
    private var httpTransport: HttpTransport? = null

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    /** OAuth 2.0 scopes.  */
    private val SCOPES = Arrays.asList(
        "https://www.googleapis.com/auth/userinfo.profile",
        "https://www.googleapis.com/auth/userinfo.email"
    )
    private var oauth2: Oauth2? = null
    private var clientSecrets: GoogleClientSecrets? = null

    /** Authorizes the installed application to access user's protected data.  */
    @Throws(Exception::class)
    private fun authorize(): Credential {
        // load client secrets
        clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY,
            InputStreamReader(OAuth2Sample::class.java.getResourceAsStream("/client_secrets.json"))
        )
        if (clientSecrets.getDetails().clientId.startsWith("Enter")
            || clientSecrets.getDetails().clientSecret.startsWith("Enter ")
        ) {
            println(
                "Enter Client ID and Secret from https://code.google.com/apis/console/ "
                        + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json"
            )
            System.exit(1)
        }
        // set up authorization code flow
        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets, SCOPES
        ).setDataStoreFactory(
            dataStoreFactory
        ).build()
        // authorize
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport()
            dataStoreFactory = FileDataStoreFactory(DATA_STORE_DIR)
            // authorization
            val credential = authorize()
            // set up global Oauth2 instance
            oauth2 =
                Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME
                ).build()
            // run commands
            tokenInfo(credential.accessToken)
            userInfo()
            // success!
            return
        } catch (e: IOException) {
            System.err.println(e.message)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        System.exit(1)
    }

    @Throws(IOException::class)
    private fun tokenInfo(accessToken: String) {
        header("Validating a token")
        val tokeninfo: Tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute()
        System.out.println(tokeninfo.toPrettyString())
        if (!tokeninfo.getAudience().equals(clientSecrets!!.details.clientId)) {
            System.err.println("ERROR: audience does not match our client ID!")
        }
    }

    @Throws(IOException::class)
    private fun userInfo() {
        header("Obtaining User Profile Information")
        val userinfo: Userinfoplus = oauth2.userinfo().get().execute()
        System.out.println(userinfo.toPrettyString())
    }

    fun header(name: String) {
        println()
        println("================== $name ==================")
        println()
    }
}
