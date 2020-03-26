package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.config.Settings
import net.micromes.core.db.DBObjects
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
import net.micromes.core.entities.guild.Guild
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URI
import java.util.*

data class UserImpl(
    private val uuid : UUID = UUID.randomUUID(),
    private val name: String,
    private var profilePictureLocation: URI = Settings.DEFAULT_LOGO_URL,
    private val status: Status = Status.OFFLINE
) : User, EntityImpl(uuid) {

    private val privateChannels: List<PrivateChannel> = listOf()
    private val publicChannels: List<PublicChannel> = listOf()
    private val guilds: MutableList<Guild> = mutableListOf()

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLName("status")
    override fun getStatus(): Status = status

    @GraphQLIgnore
    override fun getProfilePictureLocation(): URI = profilePictureLocation

    @GraphQLIgnore
    override fun getAllChannels(): List<Channel> {
        val channels : MutableList<Channel> = mutableListOf()
        channels.addAll(getPrivateChannels())
        channels.addAll(getPublicChannels())
        getGuilds().forEach {
            channels.addAll(it.getChannels())
        }
        return channels
    }

    @GraphQLIgnore
    override fun getPrivateChannels(): List<PrivateChannel> = privateChannels

    @GraphQLIgnore
    override fun getPublicChannels(): List<PublicChannel> = publicChannels

    @GraphQLIgnore
    override fun getGuilds(): List<Guild> = guilds

    @GraphQLIgnore
    override fun createPrivateChannel(name: String, uuid: UUID) {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun createPublicChannel(name: String, uuid: UUID) {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun changeProfilePictureLocation(profilePictureLocation: URI) {
        transaction {
            DBObjects.Companion.Users.update({ DBObjects.Companion.Users.id eq getUUID() }) {
                it[DBObjects.Companion.Users.profilePictureLocation] = profilePictureLocation.toString()
            }
        }
        // TODO not really necessary???
        this.profilePictureLocation = profilePictureLocation
    }

}