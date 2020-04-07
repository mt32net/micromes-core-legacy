package net.micromes.core.entities.user

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.expediagroup.graphql.annotations.GraphQLName
import net.micromes.core.entities.EntityImpl
import net.micromes.core.entities.ID
import net.micromes.core.entities.channels.*
import net.micromes.core.entities.guild.Guild
import java.net.URI

class UserDataImpl(
    private val id : ID,
    private val name: String,
    private val status: Status,
    private val profilePictureLocation: URI
) : UserImpl(
    id = id,
    status = status
) {

    @GraphQLName("name")
    override fun getName(): String = name

    @GraphQLName("status")
    override fun getStatus(): Status = status

    @GraphQLIgnore
    override fun getProfilePictureLocation(): URI = profilePictureLocation
}