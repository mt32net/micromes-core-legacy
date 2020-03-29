package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName

@GraphQLIgnore
interface Entity{

    @GraphQLID
    @GraphQLIgnore
    fun getID(): ID

    @GraphQLName("uuid")
    fun getUUIDString(): String
}