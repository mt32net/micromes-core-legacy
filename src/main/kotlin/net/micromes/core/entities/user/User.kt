package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.config.Settings
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
import java.net.URI
import java.util.*

data class User(
    @GraphQLIgnore
    private val uuid : UUID = UUID.randomUUID(),
    val name: String,
    @GraphQLIgnore
    val profilePictureLocation: URI = Settings.DEFAULT_LOGO_URL,
    val status: Status = Status.OFFLINE,
    @GraphQLIgnore
    val privateChannels: List<PrivateChannel> = listOf(),
    @GraphQLIgnore
    val publicChannels: List<PublicChannel> = listOf()
) : EntityImpl(uuid) {

    @GraphQLName("profilePictureLocation")
    fun getProfilePictureURIAsString() : String = profilePictureLocation.toASCIIString()
}