package com.deledzis.messenger.domain.model.response.user

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.user.User

data class GetUserResponse(val response: User) : Entity() {
    /*override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
        && response.id != null
        && !response.email.isNullOrBlank()
        && !response.name.isNullOrBlank()*/
}