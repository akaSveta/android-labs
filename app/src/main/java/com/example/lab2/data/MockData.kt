package com.example.lab2.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Today
import com.example.lab2.domain.Task
import com.example.lab2.domain.TaskList

object MockData {
    val baseLists: List<TaskList> = listOf(
        TaskList("1", "Сегодня", 0xFF0A84FF, 1, Icons.Default.Today),
        TaskList("2", "В планах", 0xFFFF453A, 1, Icons.Default.DateRange),
        TaskList("3", "Все", 0xFF8E8E93, 3, Icons.Default.Inbox),
        TaskList("4", "С флажком", 0xFFFF9F0A, 0, Icons.Default.Flag),
        TaskList("5", "Завершено", 0xFF8E8E93, 50, Icons.Default.Check)
    )

    val myLists: List<TaskList> = listOf(
        TaskList("6", "Напоминания", 0xFFFF9F0A, 3, Icons.Default.Menu),
    )

    val tasks: List<Task> = listOf(
        Task("t1", "Купить молоко", "Не забыть про хлеб", false, "6", priority = 1),
        Task("t2", "Позвонить маме", "", false, "6", priority = 3),
        Task("t3", "Сдать лабораторную", "До пятницы", false, "6", priority = 2),
    )
}