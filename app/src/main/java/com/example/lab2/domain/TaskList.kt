package com.example.lab2.domain

import androidx.compose.ui.graphics.vector.ImageVector

data class TaskList(
    val id: String,
    val title: String,
    val iconColor: Long,
    val taskCount: Int,
    val icon: ImageVector? = null
)