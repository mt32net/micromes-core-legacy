package net.micromes.core.entities.message

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import java.time.LocalDateTime
import java.util.*

data class ContentImpl(
    private val content : String,
    private val uuid : UUID
) : EntityImpl(uuid), Content {

    @GraphQLName("content")
    override fun getContent(): String = content

    @GraphQLIgnore
    override fun getTimeCreated(): LocalDateTime {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun getTimeUpdated(): LocalDateTime {
        TODO("Not yet implemented")
    }

}