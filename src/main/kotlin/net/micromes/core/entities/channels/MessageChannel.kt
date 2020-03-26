package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.message.Message

interface MessageChannel: Channel {

    @GraphQLIgnore
    fun getMessages() : Array<Message>
    @GraphQLIgnore
    fun sendMessage(message: Message)
}