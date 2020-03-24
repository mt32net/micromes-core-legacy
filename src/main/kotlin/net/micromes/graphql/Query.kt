package net.micromes.graphql

import net.micromes.graphql.Context

class Query {
    fun username(context: Context) = context.user.username
}