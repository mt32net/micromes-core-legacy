package net.micromes.core.db

import net.micromes.core.entities.channels.GuildChannel
import net.micromes.core.entities.guild.Guild
import org.jetbrains.exposed.sql.transactions.transaction
import net.micromes.core.db.Tables.Companion.Guilds
import net.micromes.core.db.Tables.Companion.ChannelsByGuilds
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.GuildMessageChannelImpl
import net.micromes.core.entities.guild.GuildImpl
import net.micromes.core.entities.user.Status
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserDataImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import java.net.URI

class DBGuild {
    fun createGuildAndReturnID(name: String, pictureLocation: String, ownerID: Long) : Long {
        var id: Long? = null
        transaction {
            id = Guilds.insertAndGetId {
                it[Guilds.name] = name
                it[Guilds.pictureLocation] = pictureLocation
                it[Guilds.owner] = EntityID(ownerID, Tables.Companion.Users)
            }.value
        }
        return id ?: throw Error("NO ID Back")
    }

    fun createGuildChannelAndReturnID(name: String, contentURL: String, guildID: Long) : Long {
        var id: Long? = null
        transaction {
            id = Tables.Companion.Channels.insertAndGetId {
                it[Tables.Companion.Channels.name] = name
                it[Tables.Companion.Channels.contentURL] = contentURL
                it[Tables.Companion.Channels.public] = false
            }.value
        }
        transaction {
            ChannelsByGuilds.insert {
                it[ChannelsByGuilds.channelID] = EntityID(id ?: throw Error("NO ID Back"), Tables.Companion.Channels)
                it[ChannelsByGuilds.guildID] = EntityID(guildID, Guilds)
            }
        }
        return id ?: throw Error("NO ID Back")
    }

    fun getGuildChannels(guildID: Long) : List<GuildChannel> {
        var channels: MutableList<GuildChannel> = mutableListOf()
        transaction {
            ChannelsByGuilds.select { ChannelsByGuilds.guildID eq guildID }.forEach { i ->
                Tables.Companion.Channels.select { Tables.Companion.Channels.id eq i[ChannelsByGuilds.channelID] }.forEach { j->
                    if (j[Tables.Companion.Channels.contentURL] == "false") channels.add(
                        GuildMessageChannelImpl(
                            id = ID(j[Tables.Companion.Channels.id].value),
                            channelName = j[Tables.Companion.Channels.name]
                        )
                    )
                }
            }
        }
        return channels
    }

    fun getGuildsForUser(userID: Long) : List<Guild> {
        var guilds: MutableList<Guild> = mutableListOf()
        transaction {
            Tables.Companion.UsersByGuilds.select { Tables.Companion.UsersByGuilds.user eq userID }.forEach {
                guilds.add(getGuildByID(it[Tables.Companion.UsersByGuilds.guild].value))
            }
        }
        return guilds
    }

    fun getGuildByID(guildID: Long) : Guild {
        var guild: Guild? = null
        transaction {
            Guilds.select { Guilds.id eq guildID }.forEach {
                guild = GuildImpl(
                    id = ID(guildID),
                    name = it[Guilds.name],
                    iconLocation = URI.create(it[Guilds.pictureLocation]),
                    ownerID = ID(it[Guilds.owner].value)
                )
            }
        }
        return guild ?: throw Error("NO ID Back")
    }

    fun getGuildByChannelID(channelID: Long) : Guild? {
        var guild: Guild? = null
        transaction {
            ChannelsByGuilds.select { ChannelsByGuilds.channelID eq channelID }.forEach {
                guild = getGuildByID(it[ChannelsByGuilds.guildID].value)
            }
        }
        return guild
    }

    fun getUserIDsForGuild(guildID: Long) : List<Long> {
        var ids: MutableList<Long> = mutableListOf()
        transaction {
            Tables.Companion.UsersByGuilds.select { Tables.Companion.UsersByGuilds.guild eq guildID }.forEach {
                ids.add(it[Tables.Companion.UsersByGuilds.user].value)
            }
        }
        return ids
    }

    fun getUsersForGuild(guildID: Long) : List<User> {
        val users : MutableList<User> = mutableListOf()
        getUserIDsForGuild(guildID).forEach {
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

    fun addUserToGuild(guildID: Long, userID: Long) {
        transaction {
            Tables.Companion.UsersByGuilds.insert {
                it[Tables.Companion.UsersByGuilds.guild] = EntityID(guildID, Guilds)
                it[Tables.Companion.UsersByGuilds.user] = EntityID(userID, Tables.Companion.Users)
            }
        }
    }
}