package net.micromes.core.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.UserImpl
import net.micromes.core.exceptions.InvalidToken
import java.net.URI
import java.security.PublicKey

fun getUserForToken(token: String, key: PublicKey) : UserImpl {

    try {
        val claims: Jws<Claims> = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)

        return UserImpl(
            id = ID(claims.body.subject),
            name = claims.body["name"] as String? ?: throw InvalidToken("No name in payload"),
            status = Status.ONLINE,
            profilePictureLocation = URI.create(claims.body["profilePictureURL"] as String? ?: throw InvalidToken("No profilePictureURL in Payload"))
        )
    } catch (e: JwtException) {
        e.printStackTrace()
        throw InvalidToken(e.message ?: "Invalid Token")
    }

    // I think this is done above
    //if (claims.body.expiration.after(Date())) throw InvalidToken()

}