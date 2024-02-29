package com.sn.data.data_sourse

interface ReminderScheduler {
    fun scheduleReminder(dueDateTime: Long?, noteTitle: String, noteDescription: String?)

}