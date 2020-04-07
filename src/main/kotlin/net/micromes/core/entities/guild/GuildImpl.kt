package net.micromes.core.entities.guild

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBGuild
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.GuildChannel
import net.micromes.core.entities.channels.MessageChannel
import java.net.URI

class GuildImpl(
    private val id: ID?,
    private val name: String,
    private val iconLocation: URI
) : Guild, EntityImpl(id) {

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLName("channels")
    override fun getChannels(): Array<GuildChannel> = DBGuild().getGuildChannels(getID().getValue()).toTypedArray()

    @GraphQLIgnore
    override fun createChannel(channel: GuildChannel) {
        DBGuild().createGuildChannelAndReturnID(
            name = channel.getName(),
            contentURL = if (channel is MessageChannel) "false" else "",
            guildID = getID().getValue()
        )
    }

    @GraphQLIgnore
    override fun getIconLocation(): URI = iconLocation
}