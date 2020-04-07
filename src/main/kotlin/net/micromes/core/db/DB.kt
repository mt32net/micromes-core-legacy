package net.micromes.core.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun dBConnect() {
    Database.connect(url = "jdbc:mysql://localhost:3306/micromes", driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "")
}

fun dBInit() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            Tables.Companion.UsersByChannels,
            Tables.Companion.Channels,
            Tables.Companion.Messages,
            Tables.Companion.Users,
            Tables.Companion.Guilds,
            Tables.Companion.ChannelsByGuilds,
            Tables.Companion.UsersByGuilds
        )
    }
}