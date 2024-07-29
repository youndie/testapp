package ru.wip.testapp.domain

open class Error(val message: String)

class NetworkError(message: String) : Error(message)
class ServerError(message: String) : Error(message)

