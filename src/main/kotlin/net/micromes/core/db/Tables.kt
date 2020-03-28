package net.micromes.core.db

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

class Tables {
    companion object {
        object Users : LongIdTable() {
            val name = varchar("name", 50)
            val profilePictureLocation = varchar("profilePictureLocation", 255)
            val externalID = varchar("externalid", 21).uniqueIndex()
            val lastRequestTime = datetime("lastrequesttime")
            val lastActionTime = datetime("lastactiontime")
        }
        object UsersByChannels : Table() {
            val user = reference("userid", Users)
            val channel = reference("channelid", Channels)
            override val primaryKey = PrimaryKey(user, channel, name = "key")
        }
        object Channels : LongIdTable() {
            val name = varchar("name", 20)
            val public = UsersByChannels.bool("public")
            val contentURL = UsersByChannels.varchar("content", 511)
        }
        object Messages : LongIdTable() {
            val content = varchar("content", 1999)
            val author = reference("author", Users)
            val channel = reference("messagechannels", Channels)
            val sendTime = datetime("sendtime")
        }
    }
}