package com.example.btvntuan4_2.screens

data class Subtask(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false
)

data class Attachment(
    val id: String,
    val fileName: String,
    val fileUrl: String? = null
)

data class Task(
    val id: String,
    val title: String,
    val description: String? = null,
    val status: String? = null,
    val priority: String? = null,
    val category: String? = null,
    val dueTime: String? = null,
    val subtasks: List<Subtask>? = null,
    val attachments: List<Attachment>? = null
)
