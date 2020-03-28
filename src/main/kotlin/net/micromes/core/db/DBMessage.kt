package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Messages
import net.micromes.core.entities.ID
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.message.MessageImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

fun getMessagesForChannelID(channelID: Long) : Array<Message> {
    val messages : MutableList<Message> = mutableListOf()
    transaction {
        Messages.select{ Messages.channel eq channelID }.forEach {
            messages.add(
                MessageImpl(
                    id = ID(it[Messages.id].value),
                    content = it[Messages.content],
                    timeSend = it[Messages.sendTime],
                    authorID = ID(it[Messages.author].value)
                )
            )
        }
    }
    return messages.toTypedArray()
}

fun sendMessage(channelID: Long, content: String, authorID: Long) {
    transaction {
        Messages.insert {
            it[Messages.content] = content
            it[Messages.author] = EntityID(authorID, Tables.Companion.Users)
            it[Messages.channel] = EntityID(channelID, Tables.Companion.Channels)
            it[Messages.sendTime] = LocalDateTime.now()
        }
    }
}