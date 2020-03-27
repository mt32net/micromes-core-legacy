package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Messages
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.message.MessageImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

fun getMessagesForChannelID(channelID: UUID) : Array<Message> {
    val messages : MutableList<Message> = mutableListOf()
    transaction {
        Messages.select{ Messages.channel eq channelID }.forEach {
            messages.add(
                MessageImpl(
                    uuid = it[Messages.id].value,
                    content = it[Messages.content],
                    timeSend = it[Messages.sendTime],
                    authorID = it[Messages.author].value
                )
            )
        }
    }
    return messages.toTypedArray()
}

fun sendMessage(channelID: UUID, content: String, authorID: UUID) {
    transaction {
        Messages.insert {
            it[Messages.content] = content
            it[Messages.author] = EntityID(authorID, Tables.Companion.Users)
            it[Messages.channel] = EntityID(channelID, Tables.Companion.Channels)
            it[Messages.sendTime] = LocalDateTime.now()
        }
    }
}