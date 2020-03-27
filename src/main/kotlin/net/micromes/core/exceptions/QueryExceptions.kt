package net.micromes.core.exceptions

import graphql.GraphQLError
import javax.management.Query

class WrongChannelTypeException(val msg : String = "Wrong Channel Type") : QueryException(msg = msg, rCode = 400)
class NotAuthenticatedException(val msg : String = "Google Id token not valid") : QueryException(msg, 400)
class MessageChannelNotExistentException(val msg : String = "Message Channel does not exist") : QueryException(msg, 400)