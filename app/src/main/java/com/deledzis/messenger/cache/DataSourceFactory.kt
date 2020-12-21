package com.deledzis.messenger.cache


class DataSourceFactory {
    fun getRemote(): RemoteDataSource {
        return RemoteDataSource()
    }

    fun getCache(): CacheDataSource {
        return CacheDataSource()
    }
}