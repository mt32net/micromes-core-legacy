package net.micromes.core.entities.message

import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.user.User
import java.time.LocalDateTime
import java.util.*

data class Message(
    val uuid: UUID,
    val content: String,
    val dateTime: LocalDateTime,
    val author: User
): EntityImpl(uuid)