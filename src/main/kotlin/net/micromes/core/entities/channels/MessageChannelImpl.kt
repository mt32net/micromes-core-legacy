package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.getMessagesForChannelID
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.message.MessageImpl
import java.util.*

@GraphQLIgnore
open class MessageChannelImpl(
    private var name : String,
    private val id: ID?
) : EntityImpl(id), MessageChannel {

    private var lastHash: Int = 0

    @GraphQLIgnore
    override fun getMessages(): Array<Message> = getMessagesForChannelID(getID().getValue())

    @GraphQLIgnore
    override fun sendMessage(message: MessageImpl) {
        net.micromes.core.db.sendMessage(channelID = getID().getValue(), content = message.getContent(), authorID = message.getAuthor().getID().getValue())
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