package net.micromes.entities

import java.time.LocalDateTime
import java.util.*

data class Message(
    val uuid: UUID,
    val content: String,
    val dateTime: LocalDateTime,
    val author: User
)