package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.config.Settings.Companion.DATE_TIME_FORMATTER
import net.micromes.core.entities.Entity
import net.micromes.core.entities.user.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface Message : Entity {

    @GraphQLName("content")
    fun getContent() : String

    @GraphQLIgnore
    fun getTime() : LocalDateTime

    @GraphQLName("author")
    fun getAuthor() : User

    @GraphQLName("time")
    fun getTimeStamp() : String = getTime().format(DATE_TIME_FORMATTER)
}