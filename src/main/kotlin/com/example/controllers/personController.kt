package com.example.controllers

import com.example.configs.Configuration
import com.example.models.getPersonDetails
import com.example.queries.*

class PersonController {
    // get Person/s data
    fun getAllPerson(): MutableList<getPersonDetails> {
        val loginValues = mutableListOf<getPersonDetails>()
        //Configuration().getConnection().useConnection { con -> // for jdbc
        Configuration().getConnection().connection.use { con -> // for hikariConfig
            con.prepareStatement(getAllPersonQuery)
                .use { result ->
                    result.executeQuery()
                        .use { data ->
                            while (data.next()) {
                                // getting the value of the id column
                                val personID = data.getInt("personID")
                                // getting the value of the last name column
                                val lastName = data.getString("firstName")
                                // getting the value of the first name column
                                val firstName = data.getString("lastName")
                                // getting the value of the address column
                                val address = data.getString("address")
                                // getting the value of the city column
                                val city = data.getString("city")
                                loginValues.add(getPersonDetails(personID, lastName, firstName, address, city ))
                            }
                            return loginValues
                        }
                }
        }
    }

    fun insertPerson(firstName: String, lastName: String, address: String, city: String): Int {
        //return Configuration().getConnection().useConnection { con -> // for jdbc
        return Configuration().getConnection().connection.use { con -> // for hikari config
            //PrepareStatement index start with 1
            con.prepareStatement(insertPersonQuery)
                .also {
                    it.setString(1, firstName)
                    it.setString(2, lastName)
                    it.setString(3, address)
                    it.setString(4, city)
                }
                .executeUpdate()
        }
    }

    fun insertPersonCredentials(employeeNo: Int, userName: String, password: String): Int {
        return Configuration().getConnection().connection.use { con ->
            con.prepareStatement(insertPersonCredentialQuery)
                .also {
                    it.setInt(1, employeeNo)
                    it.setString(2, userName)
                    it.setString(3, password)
                }
                .executeUpdate()
        }
    }

    fun deletePerson(idParam: Int): Int {
        //return Configuration().getConnection().useConnection { con -> // for jdbc
        return Configuration().getConnection().connection.use { con -> // for hikari config
            //PrepareStatement index start with 1
            con.prepareStatement(deletePersonQuery)
                .also {
                    it.setInt(1, idParam)
                }
                .executeUpdate()
        }
    }

    fun updatePerson(idParam: Int, lastName: String, firstName: String, address: String, city: String): Int {
        //return Configuration().getConnection().useConnection {con -> // for jdbc
        return Configuration().getConnection().connection.use {con ->
            //PrepareStatement index start with 1
            con.prepareStatement(updatePersonQuery)
                .also {
                    it.setInt(5, idParam)
                    it.setString(1, lastName)
                    it.setString(2, firstName)
                    it.setString(3, address)
                    it.setString(4, city)
                }
                .executeUpdate()
        }
    }

    fun changePassword(password: String?, idParam: Int): Int {
        return Configuration().getConnection().connection.use {con ->
            //PrepareStatement index start with 1
            con.prepareStatement(changePasswordQuery)
                .also {
                    it.setString(1, password)
                    it.setInt(2, idParam)
                }
                .executeUpdate()
        }
    }
}