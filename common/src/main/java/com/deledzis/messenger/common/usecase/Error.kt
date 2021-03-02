package com.deledzis.messenger.common.usecase

sealed class Error(val exception: Exception?) {
    class NetworkError(exception: Exception? = null) : Error(exception = exception) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class NetworkConnectionError(exception: Exception? = null) : Error(exception = exception) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class GenericError(exception: Exception? = null) : Error(exception = exception) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class ResponseError(errorCode: Int? = null, errorMessage: String? = null) : Error(
        exception = ResponseErrorException(errorCode = errorCode, errorMessage = errorMessage)
    ) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    open class PersistenceError(exception: Exception? = null) : Error(exception = exception) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class MissingInCacheError(exception: Exception? = null) :
        PersistenceError(exception = exception) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class UnsupportedOperationError(exception: Exception? = null) : Error(exception = exception) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }
}

class ResponseErrorException(val errorCode: Int?, val errorMessage: String?) : Exception() {
    override val message: String
        get() = "Error code: $errorCode, message: $errorMessage"
}