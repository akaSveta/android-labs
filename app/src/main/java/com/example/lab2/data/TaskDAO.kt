package com.example.lab2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab2.domain.Task
import com.example.lab2.domain.TaskListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isDone = 0 ORDER BY priority DESC, title ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY priority DESC, title ASC")
    suspend fun getAllTasksForFiltering(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Insert
    suspend fun insertTask(task: Task)

    @Query("UPDATE tasks SET isDone = :isDone WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, isDone: Boolean)

    @Update
    suspend fun updateFullTask(task: Task)

    @Query("SELECT * FROM task_lists ORDER BY id ASC")
    fun getAllTaskLists(): Flow<List<TaskListEntity>>

    @Insert
    suspend fun insertTaskList(taskList: TaskListEntity)

    @Query("SELECT MAX(id) FROM task_lists")
    suspend fun getMaxTaskListId(): Long?

    @Query("DELETE FROM task_lists WHERE id = :listId")
    suspend fun deleteTaskList(listId: Long)

    @Query("DELETE FROM tasks WHERE listId = :listId")
    suspend fun deleteTasksInList(listId: String)
}