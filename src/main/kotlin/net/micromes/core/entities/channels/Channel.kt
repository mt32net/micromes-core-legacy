package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.Entity

@GraphQLIgnore
interface Channel : Entity {

    fun getName(): String
    @GraphQLIgnore
    fun setName(name: String)
}