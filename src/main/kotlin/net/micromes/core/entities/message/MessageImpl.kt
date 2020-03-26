package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import java.time.LocalDateTime
import java.util.*

data class MessageImpl(
    private val uuid: UUID,
    private val content: String,
    private val dateTime: LocalDateTime,
    private val author: UserImpl
): EntityImpl(uuid), Message {

    @GraphQLName("content")
    override fun getContent(): String = content

    @GraphQLName("time")
    override fun getTime(): LocalDateTime = dateTime

    @GraphQLName("author")
    override fun getAuthor(): User = author
}