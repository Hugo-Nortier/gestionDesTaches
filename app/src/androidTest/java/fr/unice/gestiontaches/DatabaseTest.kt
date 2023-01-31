package fr.unice.gestiontaches

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.core.app.ApplicationProvider
import fr.unice.gestiontaches.dal.room.TodoDatabase
import fr.unice.gestiontaches.dal.room.dao.TodoDao
import fr.unice.gestiontaches.models.TodoLocal
import fr.unice.gestiontaches.utils.NetworkSync
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var todoDao: TodoDao
    private lateinit var database: TodoDatabase
    private lateinit var todos: MutableList<TodoLocal>

    @Before
    fun tearUp() {
        createDatabase()

        todos = mutableListOf(
            TodoLocal(
                NetworkSync.randomString(),
                "blabla",
                "Recopier les slides du prof",
                false,
                "appuyer sur ctrl+c et ctrl+v",
                "Hugo",
                "Yessine",
                "2023-12-05 23:59:59"
            ),
            TodoLocal(
                NetworkSync.randomString(),
                "blabla2",
                "Recopier les slides du prof encore",
                true,
                "contacter le prof car Heroku est down",
                "Hugo",
                "Hugo",
                "2022-01-01 23:59:59"
            )
        )

        todos.forEach {
            runBlocking {
            todoDao.insertTask(it)
        }
        }
    }

    private fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java).build()
        todoDao = database.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = database.close()

    @Test
    fun databaseIsNotEmptyAfterInsertionTest(){
        runBlocking {
            assertTrue(todoDao.getAllTasks().isNotEmpty())
        }
    }

    @Test
    fun databaseHas2RecordsTest(){
        runBlocking {
        assertEquals(2, todoDao.getAllTasks().size)
    }
    }

    @Test
    fun deleteFromDatabseTest(){
        runBlocking {
            todoDao.deleteTask(todos[0])
            todoDao.deleteTask(todos[1])
            assertTrue(todoDao.getAllTasks().isEmpty())
        }
    }

    @Test
    fun getTodoByIdTest(){
        runBlocking {
            // 2 todos (id 1L et 2L) ont été insérés, nous vérifions que la database Room les ait
            assertNotNull(todoDao.getTaskById("blabla"))
            assertNotNull(todoDao.getTaskById("blabla2"))
        }
    }

    @Test
    fun assertTodoGotIsCorrectTest(){
        runBlocking {
            val todoGotWithDao: TodoLocal = todoDao.getTaskById("blabla")
            // On vérifie que les informations réceptionnées sont correctes
            assertEquals("Yessine", todoGotWithDao.assignee)
            assertEquals("Recopier les slides du prof", todoGotWithDao.task)
            assertEquals("appuyer sur ctrl+c et ctrl+v", todoGotWithDao.description)
            assertEquals(false, todoGotWithDao.completed)
        }
    }

    @Test
    fun getTodoByTaskTest(){
        runBlocking {
            // nous vérifions que la database Room possède bien un to_do avec l'attribut task convenable
            assertNotNull(todoDao.getTasksByName("Recopier les slides du prof"))
            assertNotNull(todoDao.getTasksByName("Faire marcher Retrofit"))
        }
    }

    @Test
    fun getTodosCompletedTest(){
        runBlocking {
            // nous vérifions que la database Room possède bien un to_do terminé
            assertNotNull(todoDao.getTasksCompleted())
            assertEquals("2022-01-01 23:59:59", todoDao.getTasksCompleted()[0].due_date)
        }
    }
}