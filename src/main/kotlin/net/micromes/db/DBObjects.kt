package net.micromes.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable

class DBObjects {
    companion object {
        object Users : UUIDTable() {
            val name = varchar("name", 50)
            val profilePictureLocation = varchar("profilePictureLocation", 255)
        }
        object UserByMChannels : IntIdTable() {
            val user = reference("users", Users)
            val mChannel = reference("messagechannels", MessageChannels)
        }
        object MessageChannels : UUIDTable() {
            val name = varchar("name", 20)
        }
        object Messages : UUIDTable() {
            val content = varchar("content", 255)
            val author = reference("author", Users)
            val channel = reference("messagechannels", MessageChannels)
        }
    }
}