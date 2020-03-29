package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Users
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URI
import java.time.LocalDateTime

class DBUser {

    fun updateProfilePictures(userIDLong: Long, profilePictureLocation: String) {
        transaction {
            Tables.Companion.Users.update({ Tables.Companion.Users.id eq userIDLong }) {
                it[Tables.Companion.Users.profilePictureLocation] = profilePictureLocation
            }
        }
    }

    fun createNewUserAndReturn(user: User, googleID: String): User {
        transaction {
            val newUserID = Users.insertAndGetId {
                it[name] = user.getName()
                it[profilePictureLocation] = user.getProfilePictureURIAsString()
                it[lastActionTime] = LocalDateTime.now()
                it[lastRequestTime] = LocalDateTime.now()
            }
        }
        return user
    }

    fun getUserByID(userID: Long): User? {
        var user: User? = null
        transaction {
            Users.select { Tables.Companion.Users.id eq userID }.forEach {
                user = UserImpl(
                    id = ID(it[Users.id].value),
                    name = it[Users.name],
                    profilePictureLocation = URI.create(it[Tables.Companion.Users.profilePictureLocation]),
                    status = Status.ONLINE
                )
            }
        }
        return user
    }

}