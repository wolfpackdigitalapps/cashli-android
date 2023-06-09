package com.wolfpackdigital.cashli.shared.exceptions

import okio.IOException

class RefreshTokenExpiredException : IOException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
