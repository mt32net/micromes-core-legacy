package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Channels
import net.micromes.core.db.Tables.Companion.UsersByChannels
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DBChannel {

    /**
     * Return either content or message channels
     */
    fun getChannelByID(channelID : Long) : Channel? {
        var channel : Channel? = null
        transaction {
            Channels.select{ Channels.id eq channelID }.forEach {
                if (it[Channels.contentURL] == "false") {
                    // TOD
                    channel = MessageChannelImpl(
                        name = it[Channels.name],
                        id = ID(it[Channels.id].value)
                    )
                } else {
                    TODO("content channels")
                }
            }
        }
        return channel
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

    fun getUserIDsForChannel(channelID: Long) : List<Long> {
        val channelIDs : MutableList<Long> = mutableListOf()
        transaction {
            Tables.Companion.UsersByChannels.select { Tables.Companion.UsersByChannels.channel eq channelID }.forEach {
                channelIDs.add(it[UsersByChannels.user].value)
            }
        }
        return channelIDs
    }

    fun getChannelsForUserID(userID: Long) : List<Channel> {
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

    fun addUserToChannel(userID: Long, channelID: Long) {
        transaction {
            addLogger(StdOutSqlLogger)
            UsersByChannels.insert {
                it[UsersByChannels.channel] = EntityID(channelID, Channels)
                it[UsersByChannels.user] = EntityID(userID, Tables.Companion.Users)
            }
        }
    }
}