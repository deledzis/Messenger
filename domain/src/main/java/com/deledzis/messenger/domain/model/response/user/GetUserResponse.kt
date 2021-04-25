package com.deledzis.messenger.domain.model.response.user

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.user.User

data class GetUserResponse(val response: User) : Entity()