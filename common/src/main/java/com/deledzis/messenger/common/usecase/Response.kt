package com.deledzis.messenger.common.usecase

sealed class Response<out T, out R> {

    class Success<out T>(val successData: T) : Response<T, Nothing>()
    class Failure<out R : Error>(val errorData: R) : Response<Nothing, R>()

    sealed class State : Response<Nothing, Nothing>() {
        class Loading : State() {
            override fun equals(other: Any?): Boolean {
                return this === other
            }

            override fun hashCode(): Int {
                return System.identityHashCode(this)
            }
        }

        class Loaded : State() {
            override fun equals(other: Any?): Boolean {
                return this === other
            }

            override fun hashCode(): Int {
                return System.identityHashCode(this)
            }
        }
    }

    suspend fun handleResult(
        stateBlock: suspend (State) -> Unit = {},
        failureBlock: suspend (R) -> Unit = {},
        successBlock: suspend (T) -> Unit = {}
    ) {
        when (this) {
            is Success -> successBlock(successData)
            is Failure -> failureBlock(errorData)
            is State -> stateBlock(this)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$successData]"
            is Failure<*> -> "Failure[exception=$errorData]"
            is State -> "State[state=${this::class.java}]"
        }
    }
}