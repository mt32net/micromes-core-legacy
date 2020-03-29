package net.micromes.core.graphql

import net.micromes.core.db.getMessagesForChannelID
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.user.User
import java.util.*

class Query {

    fun me(context: Context) : User = context.getUser()
    fun privateChannels(context: Context) : List<PrivateChannel> = context.getUser().getPrivateChannels()

    fun messagesForChannel(context: Context, channelID: String) : Array<Message> {
        return getMessagesForChannelID(channelID.toLong())
    }
}