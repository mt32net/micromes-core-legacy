package net.micromes.core.exceptions

import graphql.GraphQLError
import javax.management.Query

class WrongChannelTypeException(val msg : String = "Wrong Channel Type") : QueryException(msg = msg, rCode = 400)
class InvalidToken(val msg : String = "JWT Invalid") : QueryException(msg, 401)
class MessageChannelNotExistentException(val msg : String = "Message Channel does not exist") : QueryException(msg, 400)
class UserNotFound(val msg : String = "User Not Found") : QueryException(msg, 404)