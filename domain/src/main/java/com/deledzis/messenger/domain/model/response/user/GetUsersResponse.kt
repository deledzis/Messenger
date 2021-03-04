package com.deledzis.messenger.domain.model.response.user

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.user.Users

data class GetUsersResponse(val response: Users) : Entity() {
    /*override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
        && !response.items.isNullOrEmpty()*/
}