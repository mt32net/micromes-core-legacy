package net.micromes.core.exceptions

import graphql.GraphQLError
import javax.management.Query

class WrongChannelTypeException(val msg : String = "Wrong Channel Type") : QueryException(msg = msg, rCode = 400)
class NotAuthenticatedException(val msg : String = "Google Id token not valid") : QueryException(msg, 400)
class UserNotFoundException(val msg : String = "User not found, please create one!") : QueryException(msg, 400)