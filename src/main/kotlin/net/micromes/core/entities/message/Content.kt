package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@GraphQLIgnore
interface Content : Entity {

    @GraphQLIgnore
    fun getURI() : URI

    @GraphQLName("contentLocation")
    fun getContentLocation() : String = getURI().toASCIIString()

    @GraphQLIgnore
    fun getTimeUpdated() : LocalDateTime

    @GraphQLName("updatedAt")
    fun getTimeStampUpdated() : String = getTimeUpdated().format(DateTimeFormatter.BASIC_ISO_DATE)
}