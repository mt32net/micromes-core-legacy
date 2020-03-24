package net.micromes.entities

import java.net.URI

data class GoogleAccount (
    val id: String,
    val name: String,
    val pictureLocation: String,
    val locale: String
)