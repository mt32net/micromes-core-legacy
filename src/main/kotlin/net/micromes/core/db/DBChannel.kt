package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Channels
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateMessageChannelImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class DBChannel {
    fun getMessageChannelByID(channelID : ID) : MessageChannel? {
        var messageChannel : MessageChannel? = null
        transaction {
            Channels.select{ Channels.id eq channelID.getValue() }.forEach {
                messageChannel = PrivateMessageChannelImpl(
                    channelName = it[Channels.name],
                    id = ID(it[Channels.id].value),
                    userIDs = getUsersIDsForChannel(channelID)
                )
            }
        }
        return messageChannel
    }

    fun createPrivateMessageChannel(name: String, usersIDs: Array<Long>) {
        transaction {
            val longID : Long = Channels.insertAndGetId {
                it[Channels.name] = name
                it[public] = false
                it[contentURL] = "false"
            }.value
            usersIDs.forEach { userID ->
                Tables.Companion.UsersByChannels.insert {
                    it[user] = EntityID(userID, Tables.Companion.Users)
                    it[channel] = EntityID(longID, Channels)
                }
            }
        }
    }

    fun getUsersIDsForChannel(channelID: ID) : Array<ID> {
        val userIDs : MutableList<ID> = mutableListOf()
        transaction {
            Tables.Companion.UsersByChannels.select { Tables.Companion.UsersByChannels.channel eq channelID.getValue() }.forEach {
                userIDs.add(ID(it[Tables.Companion.UsersByChannels.user].value))
            }
        }
        return userIDs.toTypedArray()
    }
}