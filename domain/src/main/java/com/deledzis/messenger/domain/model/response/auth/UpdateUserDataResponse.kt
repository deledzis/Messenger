package com.deledzis.messenger.domain.model.response.auth

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth

data class UpdateUserDataResponse(val response: Auth) : Entity() {
//    override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
//        && !response.accessToken.isNullOrBlank()
}