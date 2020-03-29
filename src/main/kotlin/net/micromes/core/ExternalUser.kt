package net.micromes.core

import net.micromes.core.entities.user.User

data class ExternalUser(
    val name: String,
    val id: Long,
    val profilePictureURI: String
) {
    constructor(user: User) : this(
        name = user.getName(),
        id = user.getID().getValue(),
        profilePictureURI = user.getProfilePictureURIAsString()
    )
}