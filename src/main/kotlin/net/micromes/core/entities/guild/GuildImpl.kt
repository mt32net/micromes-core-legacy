package net.micromes.core.entities.guild

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.db.DBChannel
import net.micromes.core.db.DBGuild
import net.micromes.core.db.DBUser
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.GuildChannel
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.DBEntityNotFoundError
import net.micromes.core.exceptions.NotWrittenYet
import java.net.URI

class GuildImpl(
    private val id: ID?,
    private val name: String,
    private val iconLocation: URI,
    private val ownerID: ID?
) : Guild, EntityImpl(id) {

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLName("channels")
    override fun getChannels(): Array<GuildChannel> = DBGuild().getGuildChannels(getID().getValue()).toTypedArray()

    @GraphQLIgnore
    override fun createChannel(channel: GuildChannel) {
        DBGuild().createGuildChannelAndReturnID(
            name = channel.getName(),
            contentURL = if (channel is MessageChannel) "false" else "",
            guildID = getID().getValue()
        )
    }

    @GraphQLIgnore
    override fun getIconLocation(): URI = iconLocation

    @GraphQLName("owner")
    override fun getOwner(): User = DBUser().getUserByID(userID = ownerID?.getValue() ?: throw NotWrittenYet()) ?: throw DBEntityNotFoundError()

    @GraphQLName("ownerID")
    override fun getOwnerIDAsString() : String = ownerID?.toString() ?: throw NotWrittenYet()

    @GraphQLName("users")
    override fun getUsers(): List<User> = DBGuild().getUsersForGuild(getID().getValue())

    @GraphQLName("userIDs")
    override fun getUserStringIDs(): List<String> = DBGuild().getUserIDsForGuild(getID().getValue()).map { id -> id.toString() }
}