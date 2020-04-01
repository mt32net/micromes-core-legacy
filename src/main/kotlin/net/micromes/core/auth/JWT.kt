package net.micromes.core.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import net.micromes.core.Payload
import net.micromes.core.exceptions.InvalidToken
import java.security.PublicKey

fun getTokenPayload(token: String, key: PublicKey) : Payload {

    try {
        val claims: Jws<Claims> = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)

        return Payload(
            sub = claims.body.subject,
            newUser = claims.body["newUser"] as Boolean,
            newName = claims.body["newName"] as String?
        )
    } catch (e: JwtException) {
        e.printStackTrace()
        throw InvalidToken(e.message ?: "Invalid Token")
    }
}