package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.exceptions.NoIDYetError

abstract class EntityImpl(
    @GraphQLIgnore
    private val id : ID?
) : Entity {

    @GraphQLIgnore
    override fun getID(): ID = id ?: throw NoIDYetError()

    @GraphQLID
    @GraphQLName("uuid")
    override fun getUUIDString() : String = getID().toString()
}