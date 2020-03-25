package net.micromes.core.entities.channels

import net.micromes.core.entities.Entity

interface Channel : Entity{

    fun getName(): String

    fun setName(name: String)
}