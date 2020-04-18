package net.micromes.core.exceptions

import graphql.GraphQLError
import javax.management.Query

class WrongChannelTypeException(val msg : String = "Wrong Channel Type") : QueryException(msg = msg, rCode = 400)
class InvalidToken(val msg : String = "JWT Invalid") : QueryException(msg, 401)
class InvalidTokenPayload(val msg : String = "JWT Payload not valid") : QueryException(msg, 400)
class NoValidAuthHeader(val msg: String = "No valid auth header") : QueryException(msg, 401)
class ChannelNotFound(val msg : String = "Channel Not Found") : QueryException(msg, 404)
class UserNotFound(val msg : String = "User Not Found") : QueryException(msg, 404)
class NotPartOfGuild(val msg: String = "Not Part of Guild"): QueryException(msg, 402)
class NoAccessToEntity(val msg: String = "Forbidden") : QueryException(msg, 402)