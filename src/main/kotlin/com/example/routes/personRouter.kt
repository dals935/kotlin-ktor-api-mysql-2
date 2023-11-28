package com.example.routes

import com.example.controllers.PersonController
import com.example.controllers.personCredentialsController
import com.example.models.*
import decrypt
import encrypt
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import io.ktor.server.application.call
import io.ktor.server.request.*
import io.ktor.server.response.*
import mnpiHash

fun Route.personRouting() {

    val personController = PersonController()
    val personCredentialsController = personCredentialsController()

    route("/person") {
        get {
            val getPersonDetails = personController.getAllPerson()
            call.respond(HttpStatusCode.OK, getPersonDetails)
        }

        get("/get/{id}") {
            try {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.OK,
                    ValidationResponse(400, false, "Missing id")
                )
                personController.getAllPerson().find { it.id == id.toInt() }.also {
                    if (it != null) return@get call.respond(HttpStatusCode.OK, it)

                    call.respond(ValidationResponse(300, false, "Item not found"))
                }
            } catch (e: Exception) {
                return@get call.respond(ValidationResponse(500, false, "Server Error: $e"))
            }
        }

        put("/update/{id}") {
            try {
                val getPersonDetails = call.receive<getPersonDetails>()
                val (id, firstName, lastName, address, city) = getPersonDetails

                val idParam = call.parameters["id"] ?: return@put call.respond(
                    HttpStatusCode.OK,
                    ValidationResponse(400, false, "Missing id")
                )
                personController.getAllPerson().find { it.id == idParam.toInt() }.also {
                    if (it != null) {
                        // do the update here and send a response
                        personController.updatePerson(idParam.toInt(), lastName, firstName, address, city)
                        return@put call.respond(ValidationResponse(200, true, "Successfully Updated $idParam"))
                    }

                    call.respond(ValidationResponse(300, false, "Item not found"))
                }
            } catch (e: Exception) {
                return@put call.respond(ValidationResponse(500, false, "Server Error: $e"))
            }
        }

        delete("/delete/{id}") {
            try {
                val idParam = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.NotFound,
                    ValidationResponse(404, false, "Not Found")
                )
                personController.getAllPerson().find { it.id == idParam.toInt() }.also {
                    if (it != null) {
                        // do the deletion here and send a response
                        val deleteValidationResponse = personController.deletePerson(idParam.toInt())
                        if (deleteValidationResponse == 1) {
                            return@delete call.respond(ValidationResponse(200, true, "Successfully Deleted $idParam"))
                        }

                        return@delete call.respond(ValidationResponse(400, false, "Deletion was unsuccessful"))
                    }

                    call.respond(ValidationResponse(300, false, "Item not found"))
                }
            } catch (e: Exception) {
                return@delete call.respond(ValidationResponse(500, false, "Server Error: $e"))
            }
        }

        post("/register") {
            try {
                val postPersonDetails = call.receive<PostPersonDetails>()
                val (firstName, lastName, address, city) = postPersonDetails

                if (firstName.isEmpty())
                    return@post call.respond(ValidationResponse(417, false, "Input First Name"))
                if (lastName.isEmpty())
                    return@post call.respond(ValidationResponse(417, false, "Input Last Name"))
                if (address.isEmpty())
                    return@post call.respond(ValidationResponse(417, false, "Input Address"))
                if (city.isEmpty())
                    return@post call.respond(ValidationResponse(417, false, "Input City"))

                //Generate Employee #
                val employeeNo = personCredentialsController.employeeNoGenerator()
                //Generate Username
                val username =
                    firstName.first().uppercase() + lastName.first().uppercase() + "-" + employeeNo.toString()
                //Generate Temp Password and Hash
                val password = "$username$employeeNo".mnpiHash()

                // do the insertion here and send a response
                personController.insertPerson(firstName, lastName, address, city)
                personController.insertPersonCredentials(employeeNo, username, password)

                return@post call.respond(ValidationResponse(200, true, "Successfully added $firstName $lastName"))
            } catch (e: Exception) {
                return@post call.respond(ValidationResponse(500, false, "Server Error: $e"))
            }
        }

        post("/change-password/{id}") {
            val postChangePassword = call.receive<PostChangePassword>()
            val (password) = postChangePassword
            val idParam = call.parameters["id"] ?: return@post call.respond(
                HttpStatusCode.NotFound,
                ValidationResponse(404, false, "Missing ID")
            )

            personController.getAllPerson().find { it.id == idParam.toInt() }.also {
                if (it != null) {
                    // do the update here and send a response
                    personController.changePassword(password.encrypt(), idParam.toInt())
                    return@post call.respond(
                        ValidationResponse(
                            201,
                            true,
                            "Successfully Changed the Password of ID: $idParam"
                        )
                    )
                }
                return@post call.respond(ValidationResponse(404, true, "$idParam Not Found"))
            }
        }
    }

    route("/login") {
        post {
            try {
                val userLoginData = call.receive<personCredentialsLogin>()
                personCredentialsController.personCredentialsLoginValidator()
                    .find {
                        it.username == userLoginData.username && it.password.decrypt() == userLoginData.password
                    }
                    ?: return@post call.respond(ValidationResponse(402, false, "Invalid Credentials"))

                return@post call.respond(ValidationResponse(200, true, "Successfully Logged In"))
            } catch (e: Exception) {
                return@post call.respond(ValidationResponse(500, false, "Server Error: $e"))
            }
        }
    }
}
