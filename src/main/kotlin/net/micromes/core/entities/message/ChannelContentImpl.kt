package net.micromes.core.entities.message

import net.micromes.core.entities.EntityImpl
import java.util.*

data class ChannelContentImpl(
    private val content : String,
    private val uuid : UUID
) : EntityImpl(uuid)