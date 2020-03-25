package net.micromes.core.entities.channels

import net.micromes.core.entities.user.UserImpl

interface PublicChannel: Channel {

    fun getOwner() : UserImpl
}