package com.sn.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sn.data.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: String): NoteEntity?

    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Query("UPDATE note SET isCompleted = :completed WHERE id = :noteId")
    suspend fun updateCompleted(noteId: String, completed: Boolean)

    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteById(noteId: String): Int

    @Query("DELETE FROM note WHERE isCompleted = 1")
    suspend fun deleteCompleted(): Int

    @Query("DELETE FROM note")
    suspend fun deleteAll()
}