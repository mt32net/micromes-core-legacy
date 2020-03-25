package net.micromes.core.entities

data class GoogleAccount (
    val id: String,
    val email: String,
    val emailVerified: Boolean,
    val name: String,
    val pictureURl: String,
    val locale: String,
    val givenName: String
)