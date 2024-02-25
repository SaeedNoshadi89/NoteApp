package com.sn.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sn.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: String): Flow<NoteEntity>

    @Upsert
    suspend fun upsert(note: NoteEntity)

    @Upsert
    suspend fun upsertAll(notes: List<NoteEntity>)

    @Query("UPDATE note SET isCompleted = :completed WHERE id = :noteId")
    suspend fun updateCompleted(noteId: String, completed: Boolean)

    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteById(noteId: String): Int

    @Query("DELETE FROM note WHERE isCompleted = 1")
    suspend fun deleteCompleted(): Int
}