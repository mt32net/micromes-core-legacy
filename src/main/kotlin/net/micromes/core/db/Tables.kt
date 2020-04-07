package net.micromes.core.db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

class Tables {
    companion object {
        object Users : LongIdTable() {
            val name = varchar("name", 50)
            val profilePictureLocation = varchar("profilePictureLocation", 255)
            val lastRequestTime = datetime("lastrequesttime")
            val lastActionTime = datetime("lastactiontime")
        }
        object UsersByChannels : Table() {
            val user = reference("userid", Users)
            val channel = reference("channelid", Channels)
        }
        object Channels : LongIdTable() {
            val name = varchar("name", 20)
            val public = bool("public").default(false)
            val contentURL = varchar("content", 255)
        }
        object Messages : LongIdTable() {
            val content = varchar("content", 1999)
            val author = reference("author", Users)
            val channel = reference("messagechannels", Channels)
            val sendTime = datetime("sendtime")
        }
        object Guilds : LongIdTable() {
            val name = varchar("name", 50)
            val pictureLocation = varchar("picturelocation", 255)
            val owner = reference("owner", Users)
        }
        object ChannelsByGuilds : Table() {
            val channelID = reference("channel", Channels)
            val guildID = reference("guild", Guilds)
            val order = integer("order").autoIncrement().default(1)
        }
        object UsersByGuilds : Table() {
            val user = reference("user", Users)
            val guild = reference("guild", Guilds)
        }
    }
}