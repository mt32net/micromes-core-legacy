package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
import net.micromes.core.entities.guild.Guild
import java.net.URI
import java.util.*

interface User : Entity {

    @GraphQLName("name")
    fun getName() : String

    @GraphQLName("status")
    fun getStatus() : Status

    @GraphQLIgnore
    fun getProfilePictureLocation() : URI

    @GraphQLName("profilePictureLocation")
    fun getProfilePictureURIAsString() : String = getProfilePictureLocation().toASCIIString()

    /**
     * Returns all private and public channels as well as guild channels the user has access to
     */
    @GraphQLIgnore
    fun getAllChannels() : List<Channel>

    @GraphQLIgnore
    fun getPrivateChannels() : List<PrivateChannel>

    @GraphQLIgnore
    fun getPublicChannels() : List<PublicChannel>

    @GraphQLIgnore
    fun getGuilds() : List<Guild>

    @GraphQLIgnore
    fun createPrivateChannel(name: String, uuid: UUID)

    @GraphQLIgnore
    fun createPublicChannel(name: String, uuid: UUID)

    @GraphQLIgnore
    fun changeProfilePictureLocation(profilePictureLocation: URI)
}