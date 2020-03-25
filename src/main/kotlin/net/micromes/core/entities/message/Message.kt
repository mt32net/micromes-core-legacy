package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.user.User
import java.time.LocalDateTime
import java.util.*

data class Message(
    @GraphQLIgnore
    private val uuid: UUID,
    val content: String,
    @GraphQLIgnore
    val dateTime: LocalDateTime,
    @GraphQLIgnore
    val author: User
): EntityImpl(uuid)