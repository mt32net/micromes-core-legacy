package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import java.util.*

interface Entity{

    @GraphQLIgnore
    fun getUUID(): UUID

    @GraphQLName("uuid")
    fun getUUIDString(): String = getUUID().toString()
}