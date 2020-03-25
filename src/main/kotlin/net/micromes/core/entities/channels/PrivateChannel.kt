package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import net.micromes.core.entities.user.User

interface PrivateChannel {
    @GraphQLIgnore
    fun getUsers(): Array<User>
    @GraphQLIgnore
    fun addUser(user: User)

    fun dummy() : String = "Hallo"
}