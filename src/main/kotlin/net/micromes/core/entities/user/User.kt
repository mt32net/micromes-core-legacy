package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBChannel
import net.micromes.core.entities.Entity
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import net.micromes.core.entities.guild.Guild
import java.net.URI

interface User : Entity {

    @GraphQLName("name")
    fun getName() : String

    @GraphQLName("status")
    fun getStatus() : Status

    @GraphQLIgnore
    fun getProfilePictureLocation() : URI

    @GraphQLName("profilePictureLocation")
    fun getProfilePictureURIAsString() : String = getProfilePictureLocation().toASCIIString()

    @GraphQLIgnore
    fun getAllChannels() : List<Channel>

    @GraphQLIgnore
    fun getPrivateChannels() : List<PrivateChannel>

    @GraphQLIgnore
    fun getPublicChannels() : List<PublicChannel>

    @GraphQLIgnore
    fun getGuilds() : List<Guild>

    @GraphQLIgnore
    fun getGuildByID(guildID: ID) : Guild?

    @GraphQLIgnore
    fun getNonGuildChannelByID(channelID: ID) : Channel?

    @GraphQLIgnore
    fun getGuildChannelByID(channelID: ID) : GuildChannel?

    @GraphQLIgnore
    fun changeName(name: String)

    @GraphQLIgnore
    fun createPrivateMessageChannel(name: String, partnerIDs: Array<ID>): PrivateMessageChannel

    @GraphQLIgnore
    fun createPublicMessageChannel(name: String)

    @GraphQLIgnore
    fun changeProfilePictureLocation(profilePictureLocation: URI)

    @GraphQLIgnore
    fun createGuild(guild: Guild)
}