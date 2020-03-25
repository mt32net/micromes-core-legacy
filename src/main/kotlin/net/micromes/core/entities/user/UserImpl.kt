package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.config.Settings
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
import java.net.URI
import java.util.*

data class UserImpl(
    private val uuid : UUID = UUID.randomUUID(),
    private val name: String,
    private val profilePictureLocation: URI = Settings.DEFAULT_LOGO_URL,
    private val status: Status = Status.OFFLINE,
    private val privateChannels: List<PrivateChannel> = listOf(),
    private val publicChannels: List<PublicChannel> = listOf()
) : User, EntityImpl(uuid) {

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLName("status")
    override fun getStatus(): Status = status

    @GraphQLIgnore
    override fun getProfilePictureLocation(): URI = profilePictureLocation

    @GraphQLIgnore
    override fun getPrivateChannels(): List<PrivateChannel> = privateChannels

    @GraphQLIgnore
    override fun getPublicChannels(): List<PublicChannel> = publicChannels

}