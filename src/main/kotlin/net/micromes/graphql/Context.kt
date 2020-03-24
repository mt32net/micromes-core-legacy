package net.micromes.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import net.micromes.entities.User

data class Context(
    val user: User
): GraphQLContext