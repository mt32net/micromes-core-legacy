package net.micromes.core.entities.channels

import net.micromes.core.entities.message.ChannelContentImpl

interface ContentChannel: Channel {

    fun getContent() : ChannelContentImpl

    fun setContent(content: ChannelContentImpl)
}