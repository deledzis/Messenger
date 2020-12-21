package com.deledzis.messenger.ui.chats

import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.cache.DataSourceFactory
import com.deledzis.messenger.data.model.chats.Chats
import com.deledzis.messenger.data.remote.ApiInterface
import com.deledzis.messenger.data.remote.NetworkManager

class ChatsRepository: BaseRepository(){
    suspend fun getChats(): Chats? {
        val respond: Chats?
        if(networkManager.isConnectedToInternet){
            respond = safeApiCall{DataSourceFactory().getRemote().getChats()}
            if (respond != null)
                respond.chats?.let { DataSourceFactory().getCache().updateChats(it) }
        }
        else
            respond = DataSourceFactory().getCache().getChats()
        return respond
    }
}