package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.message.MessageImpl
import java.util.*

abstract class MessageChannelImpl(
    private var name : String,
    private val uuid: UUID
) : EntityImpl(uuid), MessageChannel {

    private val messages: MutableList<MessageImpl> = mutableListOf()
    private var lastHash: Int = 0

    @GraphQLIgnore
    override fun getMessages(): Array<Message> = messages.toTypedArray()

    @GraphQLIgnore
    override fun sendMessage(message: MessageImpl) {
        messages.add(message)
        lastHash = message.hashCode()
    }

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLIgnore
    override fun setName(name: String) {
        this.name = name
    }

    @GraphQLIgnore
    override fun getChecksum(): Int = lastHash
}