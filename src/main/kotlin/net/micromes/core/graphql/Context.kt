package net.micromes.core.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import net.micromes.core.entities.user.UserImpl

data class Context(
    private var user : UserImpl
): GraphQLContext {
    fun getUser() = user
}