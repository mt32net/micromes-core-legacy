package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Users
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserDataImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
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

    fun createNewUser(userID: Long, userName: String, profilePictureLocation: String) {
        transaction {
            Users.insert {
                it[Users.id] = EntityID(userID, Users)
                it[name] = userName
                it[Users.profilePictureLocation] = profilePictureLocation
                it[lastActionTime] = LocalDateTime.now()
                it[lastRequestTime] = LocalDateTime.now()
            }
        }
    }

    fun getUserByID(userID: Long): User? {
        var user: User? = null
        transaction {
            Users.select { Tables.Companion.Users.id eq userID }.forEach {
                user = UserDataImpl(
                    id = ID(it[Users.id].value),
                    status = Status.ONLINE,
                    profilePictureLocation = URI.create(it[Users.profilePictureLocation]),
                    name = it[Users.name]
                )
            }
        }
        return user
    }

}