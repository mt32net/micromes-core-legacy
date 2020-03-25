package net.micromes.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import net.micromes.entities.GoogleAccount
import net.micromes.entities.User

data class Context(
    val googleAccount: GoogleAccount
): GraphQLContext {

    private var user : User

    init {
        user = User(name = "Matti")
    }

    fun getUser() = user
}