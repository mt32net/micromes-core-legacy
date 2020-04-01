package net.micromes.core

import java.util.*

class Payload(
    val sub: String,

    val newUser: Boolean = false,
    val newName: String? = null
)