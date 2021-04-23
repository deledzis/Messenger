package com.deledzis.messenger.domain.model.response.auth

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse

data class DeleteAccountResponse(val response: ServerMessageResponse) : Entity() {
//    override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
//        && !response.accessToken.isNullOrBlank()
}