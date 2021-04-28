package com.deledzis.messenger.common

object Constants {
    const val MOCK_BUILD: Boolean = true
    private const val API_VERSION: String = "v1"
    const val API_BASE_URL_MOCK: String = "http://10.0.2.2:8080/$API_VERSION/"
    const val API_BASE_URL_PROD: String = "https://spbstu-messenger.herokuapp.com/$API_VERSION/"

    const val CHATS_PERIODIC_DELAY: Long = 2000L
    const val MESSAGES_PERIODIC_DELAY: Long = 500L
}