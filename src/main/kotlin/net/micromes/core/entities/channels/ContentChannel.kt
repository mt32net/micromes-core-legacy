package net.micromes.core.entities.channels

import net.micromes.core.entities.message.ChannelContent

interface ContentChannel: Channel {

    fun getContent() : ChannelContent

    fun setContent(content: ChannelContent)
}