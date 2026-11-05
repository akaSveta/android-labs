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

    val tasks = dao.getAllTasks()

    suspend fun getActiveTasks(): List<Task> {
        return dao.getActiveTasksDirectly()
    }

    fun addTask(title: String, notes: String, priority: Int) {
        viewModelScope.launch {
            dao.insertTask(Task(title = title, notes = notes, listId = "6", priority = priority))
        }
    }

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            dao.updateTaskStatus(task.id, task.isDone)
        }
    }
}