package net.micromes.core.entities.channels

import net.micromes.core.entities.guild.Guild

interface GuildChannel: Channel {

    fun getGuild(): Guild
}