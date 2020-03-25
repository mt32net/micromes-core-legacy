package net.micromes.core.entities.channels

import net.micromes.core.entities.user.User

interface PublicChannel: Channel {

    fun getOwner() : User
}