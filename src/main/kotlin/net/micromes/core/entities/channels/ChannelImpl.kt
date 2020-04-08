package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID

abstract class ChannelImpl(
    private val id: ID?,
    private val name: String
) : EntityImpl(id), Channel {

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLIgnore
    override fun setName(name: String) {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    override fun getChecksum(): Int {
        TODO("Not yet implemented")
    }
}