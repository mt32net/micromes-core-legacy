package net.micromes.core.exceptions

abstract class QueryException(
    private val msg : String,
    private val rCode : Int
) : RuntimeException("Query Exception: $msg") {
    fun getRCode() = rCode
}