package net.micromes.core.entities.guild

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import net.micromes.core.entities.channels.Channel
import net.micromes.core.entities.channels.GuildChannel
import java.net.URI
import java.util.*

interface Guild : Entity {

    @GraphQLName("name")
    fun getName() : String

    @GraphQLName("channels")
    fun getChannels() : Array<GuildChannel>

    @GraphQLIgnore
    fun createChannel(channel: GuildChannel)

    @GraphQLIgnore
    fun getIconLocation() : URI

    @GraphQLName("iconLocation")
    fun getIconLocationAsString() : String = getIconLocation().toString()
}