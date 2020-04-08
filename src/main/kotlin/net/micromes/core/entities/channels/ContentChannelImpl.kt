package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.ID
import net.micromes.core.entities.message.Content

open class ContentChannelImpl(
    private val id: ID?,
    private val channelName: String,
    private var content: Content
) : ChannelImpl(id, channelName), ContentChannel {

    @GraphQLName("content")
    override fun getContent(): Content = content
}