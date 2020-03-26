package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.channels.ContentChannel
import java.time.LocalDateTime
import java.util.*

data class ChannelContentImpl(
    private val content : String,
    private val uuid : UUID
) : EntityImpl(uuid), ChannelContent {

    @GraphQLName("content")
    override fun getContent(): String = content

    @GraphQLName("createdAt")
    override fun getTimeCreated(): LocalDateTime {
        TODO("Not yet implemented")
    }

    @GraphQLName("lastUpdatedAt")
    override fun getTimeUpdated(): LocalDateTime {
        TODO("Not yet implemented")
    }

}