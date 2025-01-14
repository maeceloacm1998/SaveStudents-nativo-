package com.br.core.workers.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.br.core.R
import com.br.core.constants.FirestoreDbConstants
import com.br.core.notifications.NotificationsManager
import com.br.core.service.external.FirebaseClient
import com.br.core.service.sharedPreferences.SharedPreferencesBuilder
import com.br.core.utils.DateUtils
import com.br.core.workers.NotificationWorkerBuilder
import com.example.data_transfer.dto.NotificationTimelineDTO
import com.example.data_transfer.dto.asDomainModel
import com.example.data_transfer.model.NotificationTimeline
import com.example.data_transfer.model.OnFailureModel
import com.example.data_transfer.model.repository.FirebaseResponseModel
import com.google.firebase.firestore.QueryDocumentSnapshot

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val firebaseClient = FirebaseClient()

    override suspend fun doWork(): Result {
        getNotificationTimeline()
        return Result.success()
    }

    private fun getNotificationTimeline() {
        firebaseClient.getDocumentValue(FirestoreDbConstants.Collections.NOTIFICATION_TIMELINE_LIST,
            object : FirebaseResponseModel<List<QueryDocumentSnapshot>> {
                override fun onSuccess(model: List<QueryDocumentSnapshot>) {
                    val notificationList = model.map {
                        it.toObject(NotificationTimelineDTO::class.java).asDomainModel()
                    }

                    notificationList.mapIndexed { index, notification ->
                        handleNotification(
                            notification,
                            index
                        )
                    }
                }

                override fun onFailure(error: OnFailureModel) {}
            })
    }

    private fun createNotification(
        title: String,
        description: String,
        deeplink: String,
        idNotification: Int
    ) {
        val notificationBuilder = NotificationsManager(applicationContext)
        notificationBuilder.createChannel(
            NotificationWorkerBuilder.ID_CHANNEL, NotificationWorkerBuilder.NAME_CHANNEL
        )
        notificationBuilder.builderNotification(
            NotificationWorkerBuilder.ID_CHANNEL,
            title,
            deeplink,
            description,
            R.drawable.notification_logo,
            idNotification
        )
    }

    private fun handleNotification(notification: NotificationTimeline, index: Int) {
        notification.timelineList?.map { timelineItem ->
            val pushToken = SharedPreferencesBuilder.GetInstance(applicationContext)
                .getString(NotificationsManager.PUSH_TOKEN_KEY)

            if (DateUtils.isCurrentDate(timelineItem.date) && notification.pushToken == pushToken) {
                when (timelineItem.type) {
                    TIMELINE_TYPE_HOLIDAY -> {
                        createNotification(
                            applicationContext.getString(
                                R.string.title_notification_holiday,
                                timelineItem.type.uppercase()
                            ),
                            applicationContext.getString(R.string.description_notification_holiday),
                            notification.deeplink,
                            index
                        )
                    }
                    TIMELINE_TYPE_EXAM -> {
                        createNotification(
                            applicationContext.getString(
                                R.string.title_notification_exam,
                                notification.shift,
                                timelineItem.type.uppercase(),
                                notification.subjectName
                            ),
                            applicationContext.getString(
                                R.string.description_notification_exam,
                                timelineItem.subjectName
                            ),
                            notification.deeplink,
                            index
                        )
                    }
                    else -> {
                        createNotification(
                            applicationContext.getString(
                                R.string.title_notification_class,
                                notification.shift,
                                notification.subjectName
                            ),
                            applicationContext.getString(
                                R.string.description_notification_class,
                                timelineItem.subjectName
                            ),
                            notification.deeplink,
                            index
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val TIMELINE_TYPE_HOLIDAY = "Feriado"
        const val TIMELINE_TYPE_EXAM = "Prova"
    }
}