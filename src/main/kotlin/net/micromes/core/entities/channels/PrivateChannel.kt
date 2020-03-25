package net.micromes.core.entities.channels

import net.micromes.core.entities.User

interface PrivateChannel : Channel {

    fun getUsers(): Array<User>
    fun addUser(user: User)
}