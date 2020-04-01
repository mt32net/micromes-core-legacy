package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBChannel
import net.micromes.core.db.DBUser
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PrivateMessageChannel
import net.micromes.core.entities.channels.PublicChannel
import net.micromes.core.entities.guild.Guild
import net.micromes.core.exceptions.DBEntityNotFoundError
import java.net.URI

data class UserImpl(
    private val id: ID?,
    private var status: Status = Status.OFFLINE
) : User, EntityImpl(id) {

    @GraphQLName("name")
    override fun getName(): String {
        return DBUser().getUserByID(getID().getValue())?.getName() ?: throw DBEntityNotFoundError()
    }

    @GraphQLName("status")
    override fun getStatus(): Status = status

    @GraphQLIgnore
    override fun getProfilePictureLocation(): URI {
        return DBUser().getUserByID(getID().getValue())?.getProfilePictureLocation() ?: throw DBEntityNotFoundError()
    }

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
        return DBChannel().getPrivateChannelsForUserID(getID().getValue())
    }

    @GraphQLIgnore
    override fun getPublicChannels(): List<PublicChannel> {
        TODO("DB Requets")
    }

    @GraphQLIgnore
    override fun getGuilds(): List<Guild> {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun changeName(name: String) {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun createPrivateMessageChannel(name: String, partnerIDs: Array<ID>): PrivateMessageChannel {
        val ids : MutableList<Long> = mutableListOf(getID().getValue())
        partnerIDs.forEach { id: ID -> ids.add(id.getValue()) }
        return DBChannel().createPrivateMessageChannel(name, ids.toTypedArray())
    }

    @GraphQLIgnore
    override fun createPublicMessageChannel(name: String) {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun changeProfilePictureLocation(profilePictureLocation: URI) {
        DBUser().updateProfilePictures(getID().getValue(), profilePictureLocation.toString())
    }

}