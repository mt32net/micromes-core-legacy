package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.message.MessageImpl

interface MessageChannel: Channel {

    @GraphQLIgnore
    fun getMessages() : Array<Message>
    @GraphQLIgnore
    fun sendMessage(message: MessageImpl)
}