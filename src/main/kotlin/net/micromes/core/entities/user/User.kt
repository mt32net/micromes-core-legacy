package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
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

    @GraphQLName("privateChannels")
    fun getPrivateChannels() : List<PrivateChannel>

    @GraphQLName("publicChannels")
    fun getPublicChannels() : List<PublicChannel>
}