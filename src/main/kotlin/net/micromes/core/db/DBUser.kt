package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Users
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI
import java.time.LocalDateTime
import java.util.*

fun createNewUserAndReturn(user: User, googleID : String) : User {
    transaction {
        val newUserID = Users.insertAndGetId {
            it[name] = user.getName()
            it[profilePictureLocation] = user.getProfilePictureURIAsString()
            it[externalID] = googleID
            it[lastActionTime] = LocalDateTime.now()
            it[lastRequestTime] = LocalDateTime.now()
        }
    }
    return user
}

fun getUserByExternalID(googleID: String) : User? {
    var user : User? = null
    transaction {
        Users.select { Users.externalID eq googleID }.forEach {
            user = UserImpl(
                uuid = it[Users.id].value,
                name = it[Users.name],
                status = Status.ONLINE,
                profilePictureLocation = URI.create(it[Users.profilePictureLocation])
            )
        }
    }
    return user
}

fun getUserByID(userID: UUID) : User? {
    var user : User? = null
    transaction {
        Users.select { Tables.Companion.Users.id eq userID }.forEach {
            user = UserImpl(
                uuid = it[Users.id].value,
                name = it[Users.name],
                profilePictureLocation = URI.create(it[Tables.Companion.Users.profilePictureLocation]),
                status = Status.ONLINE
            )
        }
    }
    return user
}