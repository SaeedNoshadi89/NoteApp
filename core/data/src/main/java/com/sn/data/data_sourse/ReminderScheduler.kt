package com.sn.data.data_sourse

interface ReminderScheduler {
    fun scheduleReminder(noteId: String, dueDateTime: Long, noteTitle: String, noteDescription: String?)

}