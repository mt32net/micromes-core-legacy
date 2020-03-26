package net.micromes.core.exceptions

import com.expediagroup.graphql.annotations.GraphQLIgnore
import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.language.SourceLocation

abstract class QueryException(
    private val msg : String,
    private val rCode : Int
) : RuntimeException(msg) {

    fun getRCode() = rCode
}