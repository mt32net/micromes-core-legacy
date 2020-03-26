package net.micromes.core.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

class DBObjects {
    companion object {
        object Users : UUIDTable() {
            val name = varchar("name", 50)
            val profilePictureLocation = varchar("profilePictureLocation", 255)
            val externalID = varchar("externalid", 21).uniqueIndex()
            val lastRequestTime = datetime("lastrequesttime")
            val lastActionTime = datetime("lastactiontime")
        }
        object UsersByChannels : IntIdTable() {
            val user = reference("users", Users)
            val Channel = reference("messagechannels", MessageChannels)
        }
        object MessageChannels : UUIDTable() {
            val name = varchar("name", 20)
        }
        object Messages : UUIDTable() {
            val content = varchar("content", 255)
            val author = reference("author", Users)
            val channel = reference("messagechannels", MessageChannels)
            val sendTime = datetime("sendtime")
        }
    }
}