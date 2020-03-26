package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import java.util.*

@GraphQLIgnore
interface Entity{

    @GraphQLID
    @GraphQLIgnore
    fun getUUID(): UUID

    @GraphQLName("uuid")
    fun getUUIDString(): String
}