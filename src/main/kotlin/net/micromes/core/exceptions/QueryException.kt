package net.micromes.core.exceptions

import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.language.SourceLocation

abstract class QueryException(
    private val msg : String,
    private val rCode : Int
) : RuntimeException(msg) {

    fun getRCode() = rCode
}