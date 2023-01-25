package com.br.core.workers.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.br.core.R
import com.br.core.notifications.NotificationsManager
import com.br.core.workers.NotificationWorkerBuilder
import kotlinx.coroutines.delay

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        for (task in 1..30) {
            createNotification("Algoritmos", "teste ${task}", task)
            delay(TWO_HOURS_INTERVAL)
        }
        return Result.success()
    }

    private fun createNotification(title: String, description: String, idNotification: Int) {
        val notificationBuilder = NotificationsManager(applicationContext)
        notificationBuilder.createChannel(
            NotificationWorkerBuilder.ID_CHANNEL,
            NotificationWorkerBuilder.NAME_CHANNEL
        )
        notificationBuilder.builderNotification(
            NotificationWorkerBuilder.ID_CHANNEL,
            title,
            description,
            R.drawable.notification_logo,
            idNotification
        )
    }

    companion object {
        const val TWO_HOURS_INTERVAL: Long = 7200000
    }
}