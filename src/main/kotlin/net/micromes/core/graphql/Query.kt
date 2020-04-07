package net.micromes.core.graphql

import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.MessageChannel
import net.micromes.core.entities.channels.PrivateChannel
import net.micromes.core.entities.guild.Guild
import net.micromes.core.entities.message.Message
import net.micromes.core.entities.user.User
import net.micromes.core.exceptions.WrongChannelTypeException

class Query {

    fun me(context: Context) : User = context.getUser()
    fun privateChannels(context: Context) : List<PrivateChannel> = context.getUser().getPrivateChannels()
    fun guilds(context: Context) : List<Guild> = context.getUser().getGuilds()
    fun getGuildByID(context: Context, guildID: String) = context.getUser().getGuildByID(guildID = ID(guildID))

    fun messagesForChannel(context: Context, channelID: String) : Array<Message> {
        val channel = context.getUser().getChannelByID(channelID = ID(channelID))
        if (channel is MessageChannel) return channel.getMessages()
        else throw WrongChannelTypeException()
    }
}