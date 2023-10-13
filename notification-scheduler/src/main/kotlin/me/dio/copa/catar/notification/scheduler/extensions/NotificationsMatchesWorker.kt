package me.dio.copa.catar.notification.scheduler.extensions

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import me.dio.copa.catar.domain.model.MatchDomain
import java.time.Duration
import java.time.LocalDateTime

private const val NOTIFICATION_TITLE_KEY = "NOTIFICATION_TITLE_KEY"
private const val NOTIFICATION_CONTENT_KEY = "NOTIFICATION_CONTENT_KEY"

class NotificationsMatchesWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE_KEY) ?: throw IllegalArgumentException("title required")
        val content = inputData.getString(NOTIFICATION_CONTENT_KEY) ?: throw IllegalArgumentException("content required")

        context.showNotification(title, content)

        return Result.success()
    }

    companion object {
        fun start(context: Context, match: MatchDomain) {
            val (id, _, _, team1, team2, date) = match

            val initialDelay = Duration.between(LocalDateTime.now(), date).minusMinutes(5)
            val inputData = workDataOf(
                NOTIFICATION_CONTENT_KEY to "O jogo já vai começar!",
                NOTIFICATION_TITLE_KEY to "${team1.flag} VS ${team2.flag}"
            )

            WorkManager.getInstance(context)
                .enqueueUniqueWork(id, ExistingWorkPolicy.KEEP, createRequest(initialDelay, inputData))

        }

        private fun createRequest(initialDelay: Duration, inputData: Data) = OneTimeWorkRequestBuilder<NotificationsMatchesWorker>()
            .setInitialDelay(initialDelay)
            .setInputData(inputData)
            .build()

        fun cancel(context: Context, match: MatchDomain) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(match.id)
        }
    }
}