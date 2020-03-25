package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity

@GraphQLIgnore
interface Channel : Entity {

    @GraphQLName("name")
    fun getName(): String
    @GraphQLIgnore
    fun setName(name: String)
}