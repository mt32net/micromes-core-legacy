package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBChannel
import net.micromes.core.db.DBGuild
import net.micromes.core.db.DBUser
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import net.micromes.core.entities.guild.Guild
import net.micromes.core.exceptions.ChannelNotFound
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
        return DBChannel().getChannelsForUserID(getID().getValue()).filterIsInstance<PrivateChannel>()
    }

    @GraphQLIgnore
    override fun getPublicChannels(): List<PublicChannel> {
        return DBChannel().getChannelsForUserID(getID().getValue()).filterIsInstance<PublicChannel>()
    }

    @GraphQLIgnore
    override fun getGuilds(): List<Guild> = DBGuild().getGuildsForUser(getID().getValue()).toList()

    @GraphQLIgnore
    override fun getGuildByID(guildID: ID) : Guild? {
        if (!checkUserInGuild(guildID)) return null
        return DBGuild().getGuildByID(guildID.getValue())
    }

    @GraphQLIgnore
    override fun getNonGuildChannelByID(channelID: ID): Channel? {
        if (!DBChannel().getUserIDsForChannel(channelID.getValue()).contains(getID().getValue())) return null
        return DBChannel().getChannelByID(channelID.getValue())
    }

    @GraphQLIgnore
    override fun getGuildChannelByID(channelID: ID): GuildChannel? {
        val guild : Guild  = DBGuild().getGuildByChannelID(channelID.getValue()) ?: throw ChannelNotFound()
        if (checkUserInGuild(guild.getID())) return null
        return DBChannel().getChannelByID(channelID.getValue()) as GuildChannel
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

    @GraphQLIgnore
    override fun createGuild(guild: Guild) {
        val id = DBGuild().createGuildAndReturnID(
            ownerID = getID().getValue(),
            name = guild.getName(),
            pictureLocation = guild.getIconLocation().toString()
        )
        DBGuild().addUserToGuild(
            guildID = id,
            userID = getID().getValue()
        )
    }

    private fun checkUserInGuild(guildID: ID) : Boolean {
        return DBGuild().getUserIDsForGuild(guildID.getValue()).contains(getID().getValue())
    }

}