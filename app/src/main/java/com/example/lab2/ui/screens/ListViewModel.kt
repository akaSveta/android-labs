package com.example.lab2.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.AppDatabase
import com.example.lab2.domain.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.taskDao()

    val tasks = dao.getAllTasks()

    fun addTask(title: String, notes: String, priority: Int) {
        viewModelScope.launch {
            dao.insertTask(
                Task(
                    title = title,
                    notes = notes,
                    listId = "6",
                    priority = priority
                )
            )
        }
    }

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            dao.updateTask(task.copy(isDone = !task.isDone))
        }
    }
}