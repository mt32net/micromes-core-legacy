package net.micromes.graphql

import net.micromes.entities.Message
import net.micromes.entities.channels.MessageChannel
import net.micromes.entities.channels.PrivateChannel
import net.micromes.entities.channels.PrivateMessageChannelImpl
import net.micromes.exceptions.WrongChannelTypeException

class Query {

    fun username(context: Context) = context.getUser().name
    fun googleName(context: Context) = context.googleAccount.name

    fun privateChannels(context: Context) : List<PrivateChannel>  = context.getUser().privateChannels

    fun messagesForChannel(context: Context, privateChannelUUID: String) : Array<Message> {
        val privateChannel = context.getUser().privateChannels.filter { privateChannel -> privateChannel.getUUID().toString() == privateChannelUUID }[0]
        if (privateChannel is MessageChannel) return privateChannel.getMessages()
        throw WrongChannelTypeException("Not a message channel")
    }
}