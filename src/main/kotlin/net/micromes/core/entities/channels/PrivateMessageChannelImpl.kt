package net.micromes.core.entities.channels

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.Entity
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.user.User
import java.util.*

class PrivateMessageChannelImpl(

    private val uuid: UUID,
    private var name: String,
    private val users: MutableList<User>,
    private val messages : MutableList<Message>

): PrivateChannel, MessageChannel, EntityImpl(uuid) {

    @GraphQLName("users")
    override fun getUsers(): Array<User> {
        return users.toTypedArray()
    }

    @GraphQLIgnore
    override fun addUser(user: User) {
        users.add(user)
    }

    @GraphQLName("messages")
    override fun getMessages(): Array<Message> {
        return messages.toTypedArray()
    }

    @GraphQLIgnore
    override fun sendMessage(message: Message) {
        this.messages.add(message)
    }

    @GraphQLName("name")
    override fun getName(): String {
        return name
    }

    @GraphQLIgnore
    override fun setName(name: String) {
        this.name = name
    }
}