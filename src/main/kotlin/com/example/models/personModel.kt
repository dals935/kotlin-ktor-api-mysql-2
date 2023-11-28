package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ValidationResponse (
    val code: Int,
    val access: Boolean,
    val status: String
)

@Serializable
data class getPersonDetails (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String
)

@Serializable
data class PostPersonDetails(
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
)

@Serializable
data class PostChangePassword(
    val password: String
)

@Serializable
data class personCredentialsLogin(
    val username: String,
    val password: String
)