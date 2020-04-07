package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import net.micromes.core.entities.guild.Guild
import java.net.URI

class UserDataImpl(
    private val id : ID,
    private val name: String,
    private val status: Status,
    private val profilePictureLocation: URI
) : User, EntityImpl(id) {

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLName("status")
    override fun getStatus(): Status = status

    @GraphQLIgnore
    override fun getProfilePictureLocation(): URI = profilePictureLocation

    @GraphQLIgnore
    override fun getAllChannels(): List<Channel> = UserImpl(getID()).getAllChannels()

    @GraphQLIgnore
    override fun getPrivateChannels(): List<PrivateChannel> = UserImpl(getID()).getPrivateChannels()

    @GraphQLIgnore
    override fun getPublicChannels(): List<PublicChannel> = UserImpl(getID()).getPublicChannels()

    @GraphQLIgnore
    override fun getGuilds(): List<Guild> = UserImpl(getID()).getGuilds()

    @GraphQLIgnore
    override fun getGuildByID(guildID: ID) : Guild? {
        TODO("impl")
    }

    @GraphQLIgnore
    override fun getNonGuildChannelByID(channelID: ID): Channel? {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun getGuildChannelByID(channelID: ID): GuildChannel? {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun changeName(name: String) {
        UserImpl(getID()).changeName(name)
    }

    @GraphQLIgnore
    override fun createPrivateMessageChannel(name: String, partnerIDs: Array<ID>): PrivateMessageChannel {
        return UserImpl(getID()).createPrivateMessageChannel(name, partnerIDs)
    }

    @GraphQLIgnore
    override fun createPublicMessageChannel(name: String) {
        UserImpl(getID()).createPublicMessageChannel(name)
    }

    @GraphQLIgnore
    override fun changeProfilePictureLocation(profilePictureLocation: URI) {
        UserImpl(getID()).changeProfilePictureLocation(profilePictureLocation)
    }

    @GraphQLIgnore
    override fun createGuild(guild: Guild) = UserImpl(getID()).createGuild(guild)
}