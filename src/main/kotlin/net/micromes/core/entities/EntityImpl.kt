package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import java.util.*

abstract class EntityImpl(
    @GraphQLIgnore
    private val uuid : UUID
) : Entity {

    @GraphQLID
    @GraphQLIgnore
    override fun getUUID() : UUID = uuid

    @GraphQLName("uuid")
    override fun getUUIDString() : String = getUUID().toString()
}