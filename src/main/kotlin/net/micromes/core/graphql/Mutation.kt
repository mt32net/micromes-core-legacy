package net.micromes.core.graphql

import net.micromes.core.db.DBChannel
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PrivateMessageChannel
import net.micromes.core.entities.channels.PrivateMessageChannelImpl
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.MessageChannelNotExistentException
import java.net.URI

class Mutation {

    private val dbChannel = DBChannel()

    fun changeProfilePictureLocation(context: Context, profilePictureLocation: String) : User {
        context.getUser().changeProfilePictureLocation(URI.create(profilePictureLocation))
        return context.getUser()
    }

    fun changeName(context: Context, name: String) : User {
        context.getUser().changeName(name)
        return context.getUser()
    }

    fun sendMessage(context: Context, channelID: String, content: String) : MessageChannel {
        net.micromes.core.db.sendMessage(content = content, channelID = channelID.toLong(), authorID = context.getUser().getID().getValue())
        return dbChannel.getMessageChannelByID(channelID = ID(channelID)) ?: throw MessageChannelNotExistentException()
    }

    fun createPrivateMessageChannel(context: Context, name: String, partnerID: String) : PrivateChannel {
        return context.getUser().createPrivateMessageChannel(name, arrayOf(ID(partnerID)))
    }
}