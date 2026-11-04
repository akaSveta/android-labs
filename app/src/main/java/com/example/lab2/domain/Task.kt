package com.example.lab2.domain

data class Task(
    val id: String,
    val title: String,
    val notes: String = "",
    val isDone: Boolean = false,
    val listId: String = "",
    val priority: Int = 0
)