package com.deledzis.messenger.domain.model.response.auth

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth

data class LoginResponse(val response: Auth) : Entity()