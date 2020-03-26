package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import java.time.LocalDateTime

interface ChannelContent : Entity {

    @GraphQLName("content")
    fun getContent() : String

    @GraphQLName("createdAt")
    fun getTimeCreated() : LocalDateTime

    @GraphQLName("lastUpdatedAt")
    fun getTimeUpdated() : LocalDateTime
}