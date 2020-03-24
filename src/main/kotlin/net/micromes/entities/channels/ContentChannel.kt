package net.micromes.entities.channels

import net.micromes.entities.ChannelContent

interface ContentChannel: Channel {

    fun getContent() : ChannelContent

    fun setContent(content: ChannelContent)
}