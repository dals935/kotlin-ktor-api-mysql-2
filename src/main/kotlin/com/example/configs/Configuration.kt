package com.example.configs


import org.ktorm.database.Database
import com.zaxxer.hikari.HikariDataSource

class Configuration {
    //Without Hikari
    fun getConnectionMy(): Database {
        return  Database.connect(
            url = "jdbc:mysql://localhost:3306/mysqltestingdb",
            user = "msqldb",
            password = "root"
        )
    }

    //WIth Hikari
    fun getConnection(): HikariDataSource {

        HikariDataSource().also {
            // set the hikari basic config
            it.jdbcUrl = "jdbc:mysql://localhost:3306/mysqltestingdb"
            // set the username
            it.username = "msqldb"
            // set the password
            it.password = "root"

            it.poolName = "Portal"

            it.maximumPoolSize = 15

            it.minimumIdle = 0

            it.idleTimeout = 20000

            it.maxLifetime = 30000

            it.connectionTimeout = 10000

            it.connectionTestQuery = "SELECT 1"

            it.leakDetectionThreshold = 2000

            return it
        }
    }
}