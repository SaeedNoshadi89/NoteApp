package com.sn.data.data_sourse

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.sn.work.AddNoteReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderSchedulerImpl @Inject constructor(@ApplicationContext private val context: Context) :
    ReminderScheduler {
    override fun scheduleReminder(
        noteId: String,
        dueDateTime: Long,
        noteTitle: String,
        noteDescription: String?
    ) {
        if (dueDateTime < 0) {
            return
        }
        WorkManager.getInstance(context).cancelAllWorkByTag(noteId)
        val myWorkRequest = OneTimeWorkRequestBuilder<AddNoteReminderWorker>()
            .setInitialDelay(dueDateTime.div(1000), TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    AddNoteReminderWorker.KEY_NOTE_TITLE to noteTitle,
                    AddNoteReminderWorker.KEY_NOTE_DESCRIPTION to noteDescription,
                )
            )
            .addTag(noteId)
            .build()

        WorkManager.getInstance(context).enqueue(myWorkRequest)
    }
}