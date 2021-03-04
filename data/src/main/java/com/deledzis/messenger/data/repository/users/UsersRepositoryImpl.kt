package com.deledzis.messenger.data.repository.users

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.mapper.users.UserMapper
import com.deledzis.messenger.data.mapper.users.UsersMapper
import com.deledzis.messenger.data.source.users.UsersDataStoreFactory
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.domain.model.response.user.GetUserResponse
import com.deledzis.messenger.domain.model.response.user.GetUsersResponse
import com.deledzis.messenger.domain.repository.UsersRepository

/**
 * Provides an implementation of the [UsersRepository] interface for communicating to and from
 * data sources
 */
class UsersRepositoryImpl(
    private val factory: UsersDataStoreFactory,
    private val itemMapper: UserMapper,
    private val itemsMapper: UsersMapper,
    private val networkManager: BaseNetworkManager
) : UsersRepository {

    override suspend fun getUser(id: Int): Response<GetUserResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().getUser(id = id)
            var response: Response<GetUserResponse, Error> = Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        GetUserResponse(response = itemMapper.mapFromEntity(it))
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun getUsers(search: String?): Response<GetUsersResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().getUsers(search = search)
            var response: Response<GetUsersResponse, Error> = Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        GetUsersResponse(
                            response = it.let { itemsMapper.mapFromEntity(it) }
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