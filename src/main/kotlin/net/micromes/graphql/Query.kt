package net.micromes.graphql

class Query {
    fun username(context: Context) = context.user.username
}