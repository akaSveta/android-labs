package com.example.lab2.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val isDone: Boolean = false,
    val listId: String = "",
    val priority: Int = 0,
    val dueDate: String = "",
    val dueTime: String = ""
)