package com.example.lab2.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.AppDatabase
import com.example.lab2.domain.Task
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.taskDao()

    suspend fun getAllTasks(): List<Task> {
        return dao.getActiveTasksDirectly()
    }

    suspend fun getFilteredTasks(listId: String): List<Task> {
        val allTasks = dao.getAllTasksForFiltering()

        val todayCalendar = java.util.Calendar.getInstance()
        val todayStr = String.format(
            java.util.Locale.getDefault(),
            "%02d.%02d.%d",
            todayCalendar.get(java.util.Calendar.DAY_OF_MONTH),
            todayCalendar.get(java.util.Calendar.MONTH) + 1,
            todayCalendar.get(java.util.Calendar.YEAR)
        )

        val todayStrNoZero = "${todayCalendar.get(java.util.Calendar.DAY_OF_MONTH)}.${todayCalendar.get(java.util.Calendar.MONTH) + 1}.${todayCalendar.get(java.util.Calendar.YEAR)}"

        return when (listId) {
            "1" -> {
                allTasks.filter { task ->
                    if (task.isDone) return@filter false
                    task.dueDate == todayStr || task.dueDate == todayStrNoZero
                }
            }
            "2" -> {
                allTasks.filter { task ->
                    if (task.isDone) return@filter false
                    task.dueDate.isNotEmpty() && !task.isDone
                }
            }
            "4" -> {
                allTasks.filter { it.priority > 0 && !it.isDone }
            }
            "5" -> {
                allTasks.filter { it.isDone }
            }
            "3", "6" -> {
                allTasks.filter { !it.isDone }
            }
            else -> allTasks.filter { !it.isDone }
        }
    }

    fun addTask(title: String, notes: String, priority: Int, dueDate: String, dueTime: String) {
        viewModelScope.launch {
            dao.insertTask(Task(title = title, notes = notes, listId = "6", priority = priority, dueDate = dueDate, dueTime = dueTime))
        }
    }

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            dao.updateTaskStatus(task.id, task.isDone)
        }
    }

    suspend fun getAllTasksForCount(): List<Task> {
        return dao.getAllTasksForFiltering()
    }
}