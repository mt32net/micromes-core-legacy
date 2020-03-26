package net.micromes.core.db

import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI
import java.time.LocalDateTime
import net.micromes.core.db.DBObjects.Companion.Users as Users

fun dBConnect() {
    Database.connect(url = "jdbc:mysql://localhost:3306/micromes", driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "")
}

fun dBInit() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            DBObjects.Companion.UsersByChannels,
            DBObjects.Companion.MessageChannels,
            DBObjects.Companion.Messages
        )
    }
}

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
                profilePictureLocation = URI.create(it[Users.profilePictureLocation]),
                status = Status.ONLINE
            )
        }
    }
    return user
}
