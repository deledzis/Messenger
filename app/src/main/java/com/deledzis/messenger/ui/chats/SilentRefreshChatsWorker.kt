package com.deledzis.messenger.ui.chats

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.deledzis.messenger.App
import com.deledzis.messenger.util.toJson

class SilentRefreshChatsWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = App.injector.api().getChats().body()
            if (result?.chats == null) {
                Result.failure()
            } else {
                val list = toJson(result.chats)
                val outputData: Data = workDataOf("CHATS" to list)
                Result.success(outputData)
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}