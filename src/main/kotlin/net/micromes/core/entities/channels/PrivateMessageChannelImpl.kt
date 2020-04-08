package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBChannel
import net.micromes.core.entities.ID
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl

class PrivateMessageChannelImpl(
    private val id : ID,
    private val channelName : String
) : PrivateMessageChannel, MessageChannelImpl(
    id = id,
    channelName = channelName
) {

    constructor(channel: Channel) : this(
        id = channel.getID(),
        channelName = channel.getName()
    )

    @GraphQLName("users")
    override fun getUsers(): Array<User> = DBChannel().getUsersForChannel(getID().getValue()).toTypedArray()

    @GraphQLIgnore
    fun getUserIDs() : Array<ID> = DBChannel().getUserIDsForChannel(getID().getValue()).map { id -> ID(id) }.toTypedArray()

    @GraphQLName("userIDs")
    override fun getUserStringIDs() : List<String> = getUserIDs().map { uuid -> uuid.toString() }

    @GraphQLIgnore
    override fun addUser(user: UserImpl) {
        DBChannel().addUserToChannel(user.getID().getValue(), getID().getValue())
    }
}
