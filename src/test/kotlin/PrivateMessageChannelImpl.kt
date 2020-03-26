import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.message.MessageImpl
import net.micromes.core.entities.user.UserImpl
import java.util.*

class PrivateMessageChannelImpl(

    private val uuid: UUID,
    private var name: String,
    private val users: MutableList<UserImpl>,
    private val messages : MutableList<MessageImpl>

): PrivateChannel,
    MessageChannel, EntityImpl(uuid) {

    @GraphQLName("users")
    override fun getUsers(): Array<UserImpl> {
        return users.toTypedArray()
    }

    @GraphQLIgnore
    override fun addUser(user: UserImpl) {
        users.add(user)
    }

    @GraphQLName("messages")
    override fun getMessages(): Array<MessageImpl> {
        return messages.toTypedArray()
    }

    @GraphQLIgnore
    override fun sendMessage(message: MessageImpl) {
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