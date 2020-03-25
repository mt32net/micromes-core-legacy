package net.micromes.core.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import net.micromes.core.entities.GoogleAccount
import net.micromes.core.entities.user.User

data class Context(
    val googleAccount: GoogleAccount
): GraphQLContext {

    private var user : User

    init {
        user = User(name = "Matti")
    }

    fun getUser() = user
}