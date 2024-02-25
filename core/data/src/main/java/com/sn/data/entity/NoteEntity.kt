package com.sn.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "note"
)
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val dueDateTime: String,
    val isCompleted: Boolean = false,
    val category: Int?
)