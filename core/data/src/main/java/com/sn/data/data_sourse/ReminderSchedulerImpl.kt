package com.sn.data.data_sourse

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.sn.work.AddNoteReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderSchedulerImpl @Inject constructor( @ApplicationContext private val context: Context) : ReminderScheduler {
    override fun scheduleReminder(dueDateTime: Long?, noteTitle: String, noteDescription: String?) {
        if (dueDateTime == null) {
            return
        }
        val myWorkRequest = OneTimeWorkRequestBuilder<AddNoteReminderWorker>()
            .setInitialDelay(dueDateTime.div(1000), TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    AddNoteReminderWorker.KEY_NOTE_TITLE to noteTitle,
                    AddNoteReminderWorker.KEY_NOTE_DESCRIPTION to noteDescription,
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(myWorkRequest)
    }
}