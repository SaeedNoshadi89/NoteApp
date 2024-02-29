package com.sn.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AddNoteReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        NotificationHelper(context).createNotification(
            inputData.getString(KEY_NOTE_TITLE).toString(),
            inputData.getString(KEY_NOTE_DESCRIPTION) ?: "")

        return Result.success()
    }

    companion object {
        const val KEY_NOTE_DESCRIPTION = "note_description"
        const val KEY_NOTE_TITLE = "note_title"
    }
}