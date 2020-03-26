package net.micromes.core.graphql

import net.micromes.core.db.DBObjects.Companion.Users as Users
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.user.User
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URI

class Mutation {

    fun changeProfilePictureLocation(context: Context, profilePictureLocation: String) : User {
        context.getUser().changeProfilePictureLocation(URI.create(profilePictureLocation))
        return context.getUser()
    }

    fun changeName(context: Context, name: String) : User {
        context.getUser().changeName(name)
        return context.getUser()
    }
}