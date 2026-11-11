package com.example.lab2.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_lists")
data class TaskListEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val color: Long,
    val iconName: String = "Menu"
)