package net.micromes.entities

data class GoogleAccount (
    val id: String,
    val email: String,
    val emailVerified: Boolean,
    val name: String,
    val pictureURl: String,
    val locale: String,
    val familyName: String,
    val givenName: String
)