package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.guild.Guild

interface GuildChannel: Channel {

    @GraphQLIgnore
    fun getGuild(): Guild
}