package net.micromes.core.graphql

import net.micromes.core.db.DBChannel
import net.micromes.core.db.getMessagesForChannelID
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateMessageChannelImpl
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.MessageChannelNotExistentException
import java.net.URI
import java.util.*

class Mutation {

    val dbChannel = DBChannel()

    fun changeProfilePictureLocation(context: Context, profilePictureLocation: String) : User {
        context.getUser().changeProfilePictureLocation(URI.create(profilePictureLocation))
        return context.getUser()
    }

    fun changeName(context: Context, name: String) : User {
        context.getUser().changeName(name)
        return context.getUser()
    }

    fun sendMessage(context: Context, channelID: String, content: String) : MessageChannel {
        net.micromes.core.db.sendMessage(content = content, channelID = UUID.fromString(channelID), authorID = context.getUser().getUUID())
        return dbChannel.getMessageChannelByID(channelID = UUID.fromString(channelID)) ?: throw MessageChannelNotExistentException()
    }

    fun createPrivateMessageChannel(context: Context, name: String, partnerID: String) : Boolean {
        dbChannel.createPrivateMessageChannel(name = name, usersIDs = arrayOf(UUID.fromString(partnerID), context.getUser().getUUID()))
        return true
    }
}