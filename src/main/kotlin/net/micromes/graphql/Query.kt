package net.micromes.graphql

class Query {
    fun username(context: Context) = context.user.name
    fun googleName(context: Context) = context.googleAccount.name
}