package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.db.DBGuild
import net.micromes.core.entities.ID
import net.micromes.core.entities.guild.Guild
import net.micromes.core.exceptions.DBEntityNotFoundError

open class GuildMessageChannelImpl(
    private val id: ID?,
    private val channelName: String
) : GuildChannel, MessageChannelImpl(
    id = id,
    channelName = channelName
) {
    @GraphQLIgnore
    override fun getGuild(): Guild = DBGuild().getGuildByChannelID(getID().getValue()) ?: throw DBEntityNotFoundError()
}