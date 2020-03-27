package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Channels
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.MessageChannelImpl
import net.micromes.core.entities.channels.PrivateMessageChannelImpl
import net.micromes.core.entities.user.User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DBChannel {
    fun getMessageChannelByID(channelID : UUID) : MessageChannel? {
        var messageChannel : MessageChannel? = null
        transaction {
            Channels.select{ Channels.id eq channelID }.forEach {
                messageChannel = PrivateMessageChannelImpl(
                    channelName = it[Channels.name],
                    uuid = it[Channels.id].value,
                    userIDs = getUsersIDsForChannel(channelID)
                )
            }
        }
        return messageChannel
    }

    fun createPrivateMessageChannel(name: String, usersIDs: Array<UUID>) {
        transaction {
            val id : UUID = Channels.insertAndGetId {
                it[Channels.name] = name
                it[public] = false
                it[contentURL] = "false"
            }.value
            usersIDs.forEach { userID ->
                Tables.Companion.UsersByChannels.insert {
                    it[user] = EntityID(userID, Tables.Companion.Users)
                    it[channel] = EntityID(id, Channels)
                }
            }
        }
    }

    fun getUsersIDsForChannel(channelID: UUID) : Array<UUID> {
        val userIDs : MutableList<UUID> = mutableListOf()
        transaction {
            Tables.Companion.UsersByChannels.select { Tables.Companion.UsersByChannels.channel eq channelID }.forEach {
                userIDs.add(it[Tables.Companion.UsersByChannels.user].value)
            }
        }
        return userIDs.toTypedArray()
    }
}