package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBChannel
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.UserImpl

class PrivateMessageChannelImpl(
    private val id : ID,
    private val channelName : String
) : PrivateMessageChannel, MessageChannelImpl(
    id = id,
    name = channelName
) {

    @GraphQLName("users")
    override fun getUsers(): Array<UserImpl> {
        TODO("Not yet implemented")
    }

    @GraphQLIgnore
    fun getUserIDs() : Array<ID> {
        TODO("Not yet")
    }

    @GraphQLName("userIDs")
    fun getUserStringIDs() : List<String> = getUserIDs().map { uuid -> uuid.toString() }

    @GraphQLIgnore
    override fun addUser(user: UserImpl) {
        DBChannel().addUserToChannel(user.getID().getValue(), getID().getValue())
    }
}
