package net.micromes.entities.channels

import net.micromes.entities.User

interface PrivateChannel : Channel {

    fun getUsers(): Array<User>
    fun addUser(user: User)
}