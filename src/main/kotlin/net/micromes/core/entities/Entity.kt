package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName

@GraphQLIgnore
interface Entity{

    @GraphQLIgnore
    fun getID(): ID

    @GraphQLID
    @GraphQLName("id")
    fun getUUIDString() : String = getID().toString()
}