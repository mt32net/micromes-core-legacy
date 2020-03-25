package net.micromes.core.graphql

import net.micromes.core.entities.Entity
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.exceptions.WrongChannelTypeException

class Query {

    fun username(context: Context) : String = context.getUser().name
    fun googleName(context: Context) : String = context.googleAccount.name

    //fun privateChannels(context: Context) : List<PrivateChannel> = context.getUser().privateChannels

}

//fun messagesForChannel(context: Context, privateChannelUUID: String) : Array<Message> {
    //val privateChannel = context.getUser().privateChannels.filter { privateChannel -> privateChannel.getUUIDString() == privateChannelUUID }[0]
    //if (privateChannel is MessageChannel) return privateChannel.getMessages()
    //throw WrongChannelTypeException("Not a message channel")
//}