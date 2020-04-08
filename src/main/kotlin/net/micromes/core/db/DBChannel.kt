package net.micromes.core.db

import net.micromes.core.db.Tables.Companion.Channels
import net.micromes.core.db.Tables.Companion.UsersByChannels
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserDataImpl
import net.micromes.core.exceptions.DBEntityNotFoundError
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

class DBChannel {

    fun getChannelByID(channelID : Long) : Channel? {
        var channel : Channel? = null
        transaction {
            Channels.select{ Channels.id eq channelID }.forEach {
                if (it[Channels.content] == null) {
                    if (it[Channels.public]) {
                        // Public message channel
                    } else {
                        channel = MessageChannelImpl(
                            channelName = it[Channels.name],
                            id = ID(it[Channels.id].value)
                        )
                    }
                } else {
                    if (it[Channels.public]) {
                        // Public message channel
                    } else {
                        channel = ContentChannelImpl(
                            channelName = it[Channels.name],
                            id = ID(it[Channels.id].value),
                            content = DBMessage().getContentByID(it[Channels.content]!!.value) ?: throw DBEntityNotFoundError()
                        )
                    }
                }
            }
        }
        return channel
    }

    fun getUsersForChannel(channelID: Long) : List<User> {
        val users: MutableList<User> = mutableListOf()
        getUserIDsForChannel(channelID).forEach {
            transaction {
                Tables.Companion.Users.select { Tables.Companion.Users.id eq it }.forEach { us ->
                    users.add(
                        UserDataImpl(
                            name = us[Tables.Companion.Users.name],
                            profilePictureLocation = URI.create(us[Tables.Companion.Users.profilePictureLocation]),
                            status = Status.ONLINE,
                            id = ID(it)
                        )
                    )
                }
            }
        }
        return users
    }

    fun createPrivateMessageChannel(name: String, usersIDs: Array<Long>) : PrivateMessageChannel {
        var longID : Long? = null
        transaction {
            addLogger(StdOutSqlLogger)
            longID = Channels.insertAndGetId {
                it[Channels.name] = name
                it[Channels.public] = false
                it[Channels.content] = null
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
            UsersByChannels.select { UsersByChannels.user eq userID }.forEach {
                val channel: Channel = getChannelByID(it[UsersByChannels.channel].value) ?: throw DBEntityNotFoundError()
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