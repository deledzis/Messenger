package com.deledzis.messenger.data.repository.auth

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.mapper.ServerMessageResponseMapper
import com.deledzis.messenger.data.mapper.auth.AuthMapper
import com.deledzis.messenger.data.source.auth.AuthDataStoreFactory
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.domain.model.response.auth.DeleteAccountResponse
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.model.response.auth.UpdateUserDataResponse
import com.deledzis.messenger.domain.repository.AuthRepository

/**
 * Provides an implementation of the [AuthRepository] interface for communicating to and from
 * data sources
 */
class AuthRepositoryImpl(
    private val factory: AuthDataStoreFactory,
    private val authMapper: AuthMapper,
    private val serverMessageResponseMapper: ServerMessageResponseMapper,
    private val networkManager: BaseNetworkManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Response<LoginResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().login(
                username = username,
                password = password
            )
            var response: Response<LoginResponse, Error> = Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response =
                        Response.Success(LoginResponse(response = authMapper.mapFromEntity(it)))
                },
                failureBlock = {
                    response = Response.Failure(it)
                }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun register(
        username: String,
        nickname: String?,
        password: String
    ): Response<RegisterResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().register(
                username = username,
                nickname = nickname,
                password = password
            )
            var response: Response<RegisterResponse, Error> =
                Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        RegisterResponse(response = authMapper.mapFromEntity(it))
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun updateUserData(
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): Response<UpdateUserDataResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().updateUserData(
                username = username,
                nickname = nickname,
                password = password,
                newPassword = newPassword
            )
            var response: Response<UpdateUserDataResponse, Error> =
                Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        UpdateUserDataResponse(response = authMapper.mapFromEntity(it))
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun deleteAccount(username: String): Response<DeleteAccountResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().deleteAccount(username = username)
            var response: Response<DeleteAccountResponse, Error> =
                Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        DeleteAccountResponse(
                            response = serverMessageResponseMapper.mapFromEntity(it)
                        )
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }
}