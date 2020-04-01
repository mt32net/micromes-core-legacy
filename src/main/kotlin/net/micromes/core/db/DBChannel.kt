package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Channels
import net.micromes.core.db.Tables.Companion.UsersByChannels
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PrivateMessageChannel
import net.micromes.core.entities.channels.PrivateMessageChannelImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DBChannel {

    fun getMessageChannelByID(channelID : ID) : MessageChannel? {
        var messageChannel : MessageChannel? = null
        transaction {
            Channels.select{ Channels.id eq channelID.getValue() }.forEach {
                messageChannel = PrivateMessageChannelImpl(
                    channelName = it[Channels.name],
                    id = ID(it[Channels.id].value)
                )
            }
        }
        return messageChannel
    }

    fun createPrivateMessageChannel(name: String, usersIDs: Array<Long>) : PrivateMessageChannel {
        var longID : Long? = null
        transaction {
            addLogger(StdOutSqlLogger)
            longID = Channels.insertAndGetId {
                it[Channels.name] = name
                it[Channels.public] = false
                it[Channels.contentURL] = "false"
            }.value
            usersIDs.forEach { userID ->
                Tables.Companion.UsersByChannels.insert {
                    it[user] = EntityID(userID, Tables.Companion.Users)
                    it[channel] = EntityID(longID!!, Channels)
                }
            }
        }
        return PrivateMessageChannelImpl(id = ID(longID!!), channelName = name)
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

    fun getPrivateChannelsForUserID(userID: Long) : List<PrivateChannel> {
        val channels = mutableListOf<PrivateChannel>()
        transaction {
            //val channelIDs = mutableListOf<Long>()
            addLogger(StdOutSqlLogger)
            UsersByChannels.select { UsersByChannels.user eq userID }.forEach { chByUser ->
                Channels.select { Channels.id eq chByUser[UsersByChannels.channel].value }.forEach { ch ->
                    if (ch[Channels.contentURL] == "false") channels.add(PrivateMessageChannelImpl(
                        channelName = ch[Channels.name],
                        id = ID(ch[Channels.id].value)
                    ))
                }
            }
        }
        return channels
    }
}