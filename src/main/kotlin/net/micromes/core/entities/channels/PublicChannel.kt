package net.micromes.core.entities.channels

import net.micromes.core.entities.User

interface PublicChannel: Channel {

    fun getOwner() : User
}