package com.sn.domain.model

data class Note(
    val id: String,
    val title: String,
    val description: String?,
    val dueDateTime: String,
    val isCompleted: Boolean = false,
){
    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description?.isEmpty() == true
}