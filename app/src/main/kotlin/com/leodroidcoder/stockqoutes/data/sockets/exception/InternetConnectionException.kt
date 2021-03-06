package com.leodroidcoder.stockqoutes.data.sockets.exception

class InternetConnectionException : Exception {

    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable) : super(cause) {}
}