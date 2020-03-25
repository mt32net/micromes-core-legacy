package net.micromes.core.exceptions

class WrongChannelTypeException(val msg : String = "Wrong Channel Type") : QueryException(msg, 400)