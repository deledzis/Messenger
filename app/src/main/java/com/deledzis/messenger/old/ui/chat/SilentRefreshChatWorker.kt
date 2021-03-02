package com.deledzis.messenger.old.ui.chat

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.deledzis.messenger.old.App
import com.deledzis.messenger.old.util.toJson

class SilentRefreshChatWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val chatId = inputData.getInt("CHAT_ID", -1)
        if (chatId < 0) return Result.failure()

        return try {
            val result = App.injector.api().getChat(chatId).body()
            if (result.messages == null) {
                Result.failure()
            } else {
                val list = toJson(result.messages)
                val outputData: Data = workDataOf("MESSAGES" to list)
                Result.success(outputData)
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}