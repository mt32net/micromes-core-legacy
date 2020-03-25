package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLName
import java.util.*

abstract class EntityImpl(
    private val uuid : UUID = UUID.randomUUID()
) : Entity {

    override fun getUUID(): UUID = uuid

    @GraphQLName("uuid")
    override fun getUUIDString(): String = getUUID().toString()
}