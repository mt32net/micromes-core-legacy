package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBUser
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.DBEntityNotFoundError
import java.time.LocalDateTime
import java.util.*

data class MessageImpl(
    private val id: ID,
    private val content: String,
    private val timeSend: LocalDateTime,
    private val authorID: ID
): EntityImpl(id), Message {

    @GraphQLName("content")
    override fun getContent(): String = content

    @GraphQLIgnore
    override fun getTime(): LocalDateTime = timeSend

    @GraphQLName("author")
    override fun getAuthor(): User = DBUser().getUserByID(authorID) ?: throw DBEntityNotFoundError()

    @GraphQLName("authorID")
    fun getAuthorIDAsString() : String = authorID.toString()
}