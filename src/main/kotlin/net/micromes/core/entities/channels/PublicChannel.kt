package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl

interface PublicChannel: Channel {

    @GraphQLName("owner")
    fun getOwner() : User
}