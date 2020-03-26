package net.micromes.core.graphql

import net.micromes.core.entities.channels.ContentChannel
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.message.ContentImpl
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.WrongChannelTypeException

class Query {

    fun me(context: Context) : User = context.getUser()
    fun privateChannels(context: Context) : List<PrivateChannel> = context.getUser().getPrivateChannels()

    fun messagesForChannel(context: Context, channelUUID: String) : Array<Message> {
        // TODO here use sql directly (DSL), a heck of a lot of useless stuff going on
        val channel = context.getUser().getAllChannels().filter { privateChannel -> privateChannel.getUUIDString() == channelUUID }[0]
        if (channel is MessageChannel) return channel.getMessages()
        throw WrongChannelTypeException("Not a message channel")
    }

    fun contentForChannel(context: Context, channelUUID: String) : ContentImpl {
        // TODO here use sql directly (DSL), a heck of a lot of useless stuff going on
        val channel = context.getUser().getAllChannels().filter { privateChannel -> privateChannel.getUUIDString() == channelUUID }[0]
        if (channel is ContentChannel) return channel.getContent()
        throw WrongChannelTypeException("Not a content channel")
    }
}