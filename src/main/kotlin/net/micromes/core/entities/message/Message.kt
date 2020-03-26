package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import net.micromes.core.entities.user.User
import java.time.LocalDateTime

interface Message : Entity {

    @GraphQLName("content")
    fun getContent() : String

    @GraphQLName("time")
    fun getTime() : LocalDateTime

    @GraphQLName("author")
    fun getAuthor() : User
}