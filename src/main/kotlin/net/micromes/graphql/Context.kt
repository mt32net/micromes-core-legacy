package net.micromes.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import net.micromes.entities.GoogleAccount
import net.micromes.entities.User

data class Context(
    val user: User,
    val googleAccount: GoogleAccount
): GraphQLContext