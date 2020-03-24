package net.micromes.entities.channels

import net.micromes.entities.Guild

interface GuildChannel: Channel {

    fun getGuild(): Guild
}