package com.example.controllers

import com.example.configs.Configuration
import com.example.models.personCredentialsLogin
import com.example.queries.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class personCredentialsController {

    fun personCredentialsLoginValidator(): List<personCredentialsLogin> {
        Configuration().getConnection().connection.use { con ->
            con.prepareStatement(getLoginCredsQuery)
                .use { result ->
                    result.executeQuery()
                        .use {data ->
                            val loginValues = mutableListOf<personCredentialsLogin>()
                            while (data.next()) {
                                val user = data.getString("username")
                                val pass = data.getString("password")

                                loginValues.add(personCredentialsLogin(user,pass))
                            }
                            return loginValues.toList()
                        }
                }
        }
    }

    fun employeeNoGenerator(): Int {
        val employeeNoStr = (1..9999).random().toString().padStart(4, '0')
        val formatter = DateTimeFormatter.ofPattern("MMyy")
        val current = LocalDateTime.now().format(formatter)
        val empnoStr = (employeeNoStr + current.toString())

        return empnoStr.toInt()
    }

}