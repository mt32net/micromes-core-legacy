package net.micromes.core.entities.channels

import net.micromes.core.entities.message.Message

interface MessageChannel: Channel {

    fun getMessages() : Array<Message>

    fun sendMessage(message: Message)
}