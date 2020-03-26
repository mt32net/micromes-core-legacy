package net.micromes.core.graphql

import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.user.User

class Mutation {

    fun changeProfilePictureLocation(context: Context, profilePictureLocation: String) : User {
        return context.getUser()
    }
}