package fr.unice.gestiontaches.dal.room.dao

import androidx.room.*
import fr.unice.gestiontaches.models.TodoLocal

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    suspend fun getAllTasks(): List<TodoLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(vararg task: TodoLocal)

    @Update
    suspend fun updateTask(task: TodoLocal)

    @Delete
    suspend fun deleteTask(task: TodoLocal)

    @Query("SELECT * FROM todos WHERE _id=:id")
    suspend fun getTaskById(id: String): TodoLocal

    @Query("SELECT * FROM todos WHERE completed = 1")
    suspend fun getTasksCompleted(): List<TodoLocal>

    @Query("SELECT * FROM todos WHERE task = :task")
    suspend fun getTasksByName(task: String): List<TodoLocal>

    @Query("DELETE FROM todos")
    suspend fun deleteAllTasks()
}