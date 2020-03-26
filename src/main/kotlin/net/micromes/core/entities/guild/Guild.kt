package net.micromes.core.entities.guild

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import net.micromes.core.entities.channels.Channel
import java.util.*

interface Guild : Entity {

    @GraphQLName("name")
    fun getName() : String

    @GraphQLName("channels")
    fun getChannels() : Array<Channel>

    @GraphQLIgnore
    fun createChannel(name: String, uuid: UUID)
}