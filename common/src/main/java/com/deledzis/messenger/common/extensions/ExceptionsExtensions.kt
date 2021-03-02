package com.deledzis.messenger.common.extensions

import com.deledzis.messenger.common.usecase.ResponseErrorException

val Exception?.asResponseError: ResponseErrorException?
    get() = if (this is ResponseErrorException) this else null

val ResponseErrorException.isAuthError: Boolean
    get() = this.errorCode == 10 && this.errorMessage?.contains(
        "auth error",
        ignoreCase = true
    ) == true