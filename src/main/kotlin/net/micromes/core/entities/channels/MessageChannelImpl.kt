package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.db.DBMessage
import net.micromes.core.entities.ID
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.message.MessageImpl

@GraphQLIgnore
open class MessageChannelImpl(
    private var channelName : String,
    private val id: ID?
) : ChannelImpl(id, channelName), MessageChannel {

    @GraphQLIgnore
    override fun getMessages(): Array<Message> = DBMessage().getMessagesForChannelID(getID().getValue())

    @GraphQLIgnore
    override fun sendMessage(message: MessageImpl) {
        DBMessage().sendMessage(channelID = getID().getValue(), content = message.getContent(), authorID = message.getAuthor().getID().getValue())
    }
}