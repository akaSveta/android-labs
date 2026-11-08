package com.example.lab2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab2.domain.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isDone = 0 ORDER BY priority DESC, title ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isDone = 0 ORDER BY priority DESC, title ASC")
    suspend fun getActiveTasksDirectly(): List<Task>

    @Insert
    suspend fun insertTask(task: Task)

    @Query("UPDATE tasks SET isDone = :isDone WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, isDone: Boolean)

    @Query("SELECT * FROM tasks ORDER BY priority DESC, title ASC")
    suspend fun getAllTasksForFiltering(): List<Task>
}