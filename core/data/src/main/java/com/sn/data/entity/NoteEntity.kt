package com.sn.domain.model

import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Long,
    val title: String,
    val description: String?,
    val dueDateTime: LocalDateTime,
    val isCompleted: Boolean = false,
    val category: String?
)