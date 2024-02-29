package com.sn.data.data_sourse

import com.sn.data.entity.NoteEntity
import com.sn.data.ext.toModel
import com.sn.data.local.database.NoteDao
import com.sn.domain.model.Note
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val noteDao: NoteDao) : LocalDataSource {

    private val mutex = Mutex()
    override suspend fun getAllNotes(categoryId: Int?): List<Note> = mutex.withLock {
        return noteDao.getAllNotes(categoryId).toModel()
    }

    override suspend fun upsertNote(note: NoteEntity) = mutex.withLock {
        noteDao.upsertNote(note)
    }

    override suspend fun deleteNote(noteId: String): Int = mutex.withLock {
        noteDao.deleteById(noteId)
    }


    override suspend fun completeNote(noteId: String) = mutex.withLock {
        noteDao.updateCompleted(noteId = noteId, completed = true)
    }

    override suspend fun activateNote(noteId: String) = mutex.withLock {
        noteDao.updateCompleted(noteId = noteId, completed = false)
    }

    override suspend fun getNoteById(noteId: String): NoteEntity? = mutex.withLock {
        return noteDao.getNoteById(noteId = noteId)
    }


    override suspend fun clearCompletedNotes(): Int = mutex.withLock {
        noteDao.deleteCompleted()
    }

    override suspend fun deleteAllNotes() = mutex.withLock {
        noteDao.deleteAll()
    }

}