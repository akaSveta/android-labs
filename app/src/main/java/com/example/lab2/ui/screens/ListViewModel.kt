package com.example.lab2.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.AppDatabase
import com.example.lab2.domain.Task
import com.example.lab2.domain.TaskListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.taskDao()

    val taskLists: Flow<List<TaskListEntity>> = dao.getAllTaskLists()

    fun addTaskList(title: String, color: Long, iconName: String) {
        viewModelScope.launch {
            val maxId = dao.getMaxTaskListId()

            val nextId = if (maxId == null || maxId < 6) 7L else maxId + 1

            dao.insertTaskList(
                TaskListEntity(
                    id = nextId,
                    title = title,
                    color = color,
                    iconName = iconName
                )
            )
        }
    }

    suspend fun getAllTasksForCount(): List<Task> {
        return dao.getAllTasksForFiltering()
    }

    suspend fun getFilteredTasks(listId: String): List<Task> {
        val allTasks = dao.getAllTasksForFiltering()
        val todayCalendar = Calendar.getInstance()
        val todayStr = String.format(Locale.getDefault(), "%02d.%02d.%d", todayCalendar.get(Calendar.DAY_OF_MONTH), todayCalendar.get(Calendar.MONTH) + 1, todayCalendar.get(Calendar.YEAR))
        val todayStrNoZero = "${todayCalendar.get(Calendar.DAY_OF_MONTH)}.${todayCalendar.get(Calendar.MONTH) + 1}.${todayCalendar.get(Calendar.YEAR)}"

        return when (listId) {
            "1" -> allTasks.filter { !it.isDone && (it.dueDate == todayStr || it.dueDate == todayStrNoZero) }
            "2" -> allTasks.filter { !it.isDone && it.dueDate.isNotEmpty() }
            "3" -> allTasks.filter { !it.isDone }
            "4" -> allTasks.filter { !it.isDone && it.priority > 0 }
            "5" -> allTasks.filter { it.isDone }
            "6" -> allTasks.filter { !it.isDone && it.listId == "6" }
            else -> allTasks.filter { !it.isDone && it.listId == listId }
        }
    }

    fun addTask(title: String, notes: String, priority: Int, dueDate: String, dueTime: String, targetListId: String) {
        viewModelScope.launch {
            dao.insertTask(
                Task(
                    title = title,
                    notes = notes,
                    listId = targetListId,
                    priority = priority,
                    dueDate = dueDate,
                    dueTime = dueTime
                )
            )
        }
    }

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            dao.updateTaskStatus(task.id, task.isDone)
        }
    }

    suspend fun searchTasks(query: String): List<Task> {
        val allTasks = dao.getAllTasksForFiltering()
        return allTasks.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.notes.contains(query, ignoreCase = true)
        }
    }
}