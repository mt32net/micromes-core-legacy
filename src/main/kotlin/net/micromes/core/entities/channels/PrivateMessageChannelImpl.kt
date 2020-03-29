package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.UserImpl
import java.util.*

class PrivateMessageChannelImpl(
    private val id : ID,
    private val channelName : String,
    private val userIDs : Array<ID>
) : PrivateChannel, MessageChannel, MessageChannelImpl(
    id = id,
    name = channelName
) {

    @GraphQLName("users")
    override fun getUsers(): Array<UserImpl> {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    fun getUserIDs() : Array<ID> = userIDs

    @GraphQLName("userIDs")
    fun getUserStringIDs() : List<String> = getUserIDs().map { uuid -> uuid.toString() }

    @GraphQLIgnore
    override fun addUser(user: UserImpl) {
        TODO("Not yet implemented")
    }
}
