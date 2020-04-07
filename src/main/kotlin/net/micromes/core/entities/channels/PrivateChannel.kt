package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl

interface PrivateChannel : Channel {

    @GraphQLName("users")
    fun getUsers(): Array<User>

    @GraphQLIgnore
    fun addUser(user: UserImpl)

    @GraphQLName("userIDs")
    fun getUserStringIDs() : List<String>
}