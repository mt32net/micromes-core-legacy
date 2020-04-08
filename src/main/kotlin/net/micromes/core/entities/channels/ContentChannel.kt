package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.message.Content

interface ContentChannel: Channel {

    @GraphQLName("content")
    fun getContent() : Content
}