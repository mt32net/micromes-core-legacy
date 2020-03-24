package net.micromes.entities

import net.micromes.config.Settings
import net.micromes.entities.channels.PrivateChannel
import net.micromes.entities.channels.PublicChannel
import java.net.URI
import java.util.*

data class User(
    val uuid: UUID = UUID.randomUUID(),
    val name: String,
    val profilePictureLocation: URI = Settings.DEFAULT_LOGO_URL,
    val status: Status = Status.OFFLINE,
    val privateChannels: List<PrivateChannel> = listOf(),
    val publicChannels: List<PublicChannel> = listOf()
)