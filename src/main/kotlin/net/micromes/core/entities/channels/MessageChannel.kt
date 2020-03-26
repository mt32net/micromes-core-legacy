package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.message.MessageImpl

interface MessageChannel: Channel {

    @GraphQLIgnore
    fun getMessages() : Array<MessageImpl>
    @GraphQLIgnore
    fun sendMessage(message: MessageImpl)
}