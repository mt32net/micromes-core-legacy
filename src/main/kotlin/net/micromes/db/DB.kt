package net.micromes.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun dBConnect() {
    Database.connect(url = "jdbc:mysql://localhost:3306/micromes", driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "")
}

fun dBInit() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            DBObjects.Companion.UserByMChannels,
            DBObjects.Companion.UserByMChannels,
            DBObjects.Companion.MessageChannels,
            DBObjects.Companion.Messages
        )
    }
}
