package net.micromes.entities.channels

import java.util.*

interface Channel {
    fun getUUID(): UUID
    fun getName(): String

    fun setName(name: String)
}