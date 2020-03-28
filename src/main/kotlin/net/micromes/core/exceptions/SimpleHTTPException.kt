package net.micromes.core.exceptions

import java.lang.RuntimeException

class SimpleHTTPException(
    val rCode : Int
) : RuntimeException()