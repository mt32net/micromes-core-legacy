package net.micromes.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun connect() {
    Database.connect(url = "jdbc:mysql://localhost:3306/micromes", driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "")
}

fun init() {
    transaction {
        addLogger(StdOutSqlLogger)
    }
}
