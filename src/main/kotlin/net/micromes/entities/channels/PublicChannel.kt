package net.micromes.entities.channels

import net.micromes.entities.User

interface PublicChannel: Channel {

    fun getOwner() : User
}