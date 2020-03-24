package net.micromes.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable

class DBObjects {
    companion object {
        object StarWarsFilms : UUIDTable() {
            val sequelId = integer("sequel__id").uniqueIndex()
            val name = varchar("name", 50)
            val director = varchar("director", 50)
        }
    }
}