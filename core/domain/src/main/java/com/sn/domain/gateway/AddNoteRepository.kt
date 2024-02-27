package com.sn.domain.gateway


interface AddNoteRepository {

    suspend fun createNote(
        title: String,
        description: String,
        dueDateTime: String,
    ): String

}