package net.micromes.entities.channels

import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.entities.Message
import net.micromes.entities.User
import java.util.*

class PrivateMessageChannelImpl(

    private val uuid: UUID,
    private var name: String,
    private val users: MutableList<User>,
    private val messages : MutableList<Message>

): PrivateChannel, MessageChannel {

    @GraphQLName("users")
    override fun getUsers(): Array<User> {
        return users.toTypedArray()
    }

    override fun addUser(user: User) {
        users.add(user)
    }

    @GraphQLName("messages")
    override fun getMessages(): Array<Message> {
        return messages.toTypedArray()
    }

    override fun sendMessage(message: Message) {
        this.messages.add(message)
    }

    @GraphQLName("uuid")
    override fun getUUID(): UUID {
        return uuid
    }

    @GraphQLName("name")
    override fun getName(): String {
        return name
    }

    override fun setName(name: String) {
        this.name = name
    }
}