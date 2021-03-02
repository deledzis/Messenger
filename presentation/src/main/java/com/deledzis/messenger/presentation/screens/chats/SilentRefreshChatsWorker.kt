package com.deledzis.messenger.presentation.screens.chats

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class SilentRefreshChatsWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = App.injector.api().getChats().body()
            if (result.chats == null) {
                Result.failure()
            } else {
                val list = toJson(result.chats)
                val outputData: Data = workDataOf(CHATS_KEY to list)
                Result.success(outputData)
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val CHATS_KEY = "CHATS"
    }
}