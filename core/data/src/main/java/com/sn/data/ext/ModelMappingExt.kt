package com.sn.data.ext

import com.sn.data.entity.NoteEntity
import com.sn.domain.model.Note

fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    dueDateTime = dueDateTime,
)

fun NoteEntity.toModel() = Note(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    dueDateTime = dueDateTime,
)

@JvmName("entityToModel")
fun List<NoteEntity>.toModel() = map(NoteEntity::toModel)
