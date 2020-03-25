package net.micromes.core.entities

import net.micromes.core.config.Settings
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.channels.PublicChannel
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