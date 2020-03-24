package net.micromes.entities.channels

import net.micromes.entities.Message

interface MessageChannel: Channel {

    fun getMessages() : Array<Message>

    fun sendMessage(message: Message)
}