package net.micromes.core.graphql

import net.micromes.core.config.Settings
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import net.micromes.core.entities.guild.GuildImpl
import net.micromes.core.entities.message.MessageImpl
import net.micromes.core.entities.user.User
import net.micromes.core.entities.user.UserImpl
import net.micromes.core.exceptions.ChannelNotFound
import net.micromes.core.exceptions.NotPartOfGuild
import net.micromes.core.exceptions.WrongChannelTypeException
import java.net.URI

class Mutation {

    fun changeProfilePictureLocation(context: Context, profilePictureLocation: String) : User {
        context.getUser().changeProfilePictureLocation(URI.create(profilePictureLocation))
        return context.getUser()
    }

    fun changeName(context: Context, name: String) : User {
        context.getUser().changeName(name)
        return context.getUser()
    }

    fun sendMessage(context: Context, channelID: String, content: String) : Boolean {
        val channel : Channel = context.getUser().getChannelByID(ID(channelID)) ?: throw ChannelNotFound()
        if (channel is MessageChannel) channel.sendMessage(MessageImpl(
            id = null,
            content = content,
            authorID = context.getUser().getID(),
            timeSend = null
        ))
        else throw WrongChannelTypeException()
        return true
    }

    fun createPrivateMessageChannel(context: Context, name: String, partnerID: String) : PrivateChannel {
        return context.getUser().createPrivateMessageChannel(name, arrayOf(ID(partnerID)))
    }

    fun addUserToChannel(context: Context, userID: String, channelID: String) : Boolean {
        val channel : Channel = context.getUser().getNonGuildChannelByID(ID(channelID)) ?: throw ChannelNotFound()
        if (channel is PrivateChannel) channel.addUser(UserImpl(ID(userID)))
        else throw WrongChannelTypeException()
        return true
    }

    fun createGuild(context: Context, name: String) : Boolean {
        context.getUser().createGuild(GuildImpl(name = name, iconLocation = Settings.STANDARD_GUILD_ICON, id = null, ownerID = null))
        return true
    }

    fun createGuildChannel(context: Context, guildID: Long, name: String) : GuildChannel {
        val newChannel = GuildMessageChannelImpl(
            channelName = name,
            id = null
        )
        context.getUser().getGuildByID(guildID = ID(guildID))?.createChannel(newChannel) ?: throw NotPartOfGuild()
        return newChannel
    }
}