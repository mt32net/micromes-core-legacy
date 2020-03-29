package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.ExternalUser
import net.micromes.core.config.Settings
import net.micromes.core.db.DBUser
import net.micromes.core.db.Tables
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
import net.micromes.core.entities.guild.Guild
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URI
import java.util.*

data class UserImpl(
    private val id: ID?,
    private var name: String,
    private var profilePictureLocation: URI = Settings.DEFAULT_LOGO_URL,
    private var status: Status = Status.OFFLINE
) : User, EntityImpl(id) {

    constructor(externalUser: ExternalUser) : this(
        id = null,
        name = externalUser.name,
        profilePictureLocation = URI.create(externalUser.profilePictureURI)
    )

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
    override fun getPrivateChannels(): List<PrivateChannel> {
        //TODO db request
        return listOf()
    }

    @GraphQLIgnore
    override fun getPublicChannels(): List<PublicChannel> {
        //TODO db request
        return listOf()
    }

    @GraphQLIgnore
    override fun getGuilds(): List<Guild> = guilds

    @GraphQLIgnore
    override fun changeName(name: String) {

        this.name = name
    }

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

        DBUser().updateProfilePictures(getID().getValue(), profilePictureLocation.toString())
        // TODO not really necessary???
        this.profilePictureLocation = profilePictureLocation
    }

}