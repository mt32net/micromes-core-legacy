package net.micromes.core.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl

data class Context(
    private var user : User
): GraphQLContext {
    fun getUser() = user
}