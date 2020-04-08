package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.exceptions.NotWrittenYet

@GraphQLIgnore
abstract class EntityImpl(
    @GraphQLIgnore
    private val id : ID?
) : Entity {

    @GraphQLIgnore
    override fun getID(): ID = id ?: throw NotWrittenYet()
}