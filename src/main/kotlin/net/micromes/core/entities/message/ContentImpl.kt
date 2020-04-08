package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import java.net.URI
import java.time.LocalDateTime

data class ContentImpl(
    private val contentURI : URI,
    private val contentID : ID?,
    private val updated: LocalDateTime
) : EntityImpl(contentID), Content {

    @GraphQLIgnore
    override fun getURI(): URI = contentURI

    @GraphQLIgnore
    override fun getTimeUpdated(): LocalDateTime = updated
}