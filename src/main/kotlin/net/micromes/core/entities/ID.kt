package net.micromes.core.entities

import com.expediagroup.graphql.annotations.GraphQLName

data class ID (private val value : Long) {
    constructor(value : String) : this(value.toLong())

    @GraphQLName("stringID")
    override fun toString(): String = getValue().toString()

    @GraphQLName("valueLong")
    fun getValue() : Long = value
}