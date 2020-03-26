package net.micromes.core.entities.channels

import net.micromes.core.entities.message.ContentImpl

interface ContentChannel: Channel {

    fun getContent() : ContentImpl

    fun setContent(content: ContentImpl)
}