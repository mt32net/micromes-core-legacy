package net.micromes.core

import net.micromes.core.entities.user.User

data class ExternalUser(
    val name: String,
    val id: String,
    val profilePictureURI: String
) {
    constructor(user: User) : this(
        name = user.getName(),
        id = user.getID().toString(),
        profilePictureURI = user.getProfilePictureURIAsString()
    )
}