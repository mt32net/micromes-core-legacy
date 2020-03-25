package net.micromes.graphql

class Query {
    fun username(context: Context) = context.getUser().name
    fun googleName(context: Context) = context.googleAccount.name
}