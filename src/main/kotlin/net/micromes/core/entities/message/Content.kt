package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface Content : Entity {

    @GraphQLName("content")
    fun getContent() : String

    @GraphQLIgnore
    fun getTimeCreated() : LocalDateTime

    @GraphQLIgnore
    fun getTimeUpdated() : LocalDateTime

    @GraphQLName("createdAt")
    fun getTimeStampCreated() : String = getTimeCreated().format(DateTimeFormatter.BASIC_ISO_DATE)

    @GraphQLName("updatedAt")
    fun getTimeStampUpdated() : String = getTimeUpdated().format(DateTimeFormatter.BASIC_ISO_DATE)
}