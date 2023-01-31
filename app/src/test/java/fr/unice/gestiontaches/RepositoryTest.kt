package fr.unice.gestiontaches

import fr.unice.gestiontaches.dal.repositories.TodoRepository
import fr.unice.gestiontaches.models.dto.TodoDto
import fr.unice.gestiontaches.utils.NetworkSync
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private lateinit var todos: MutableList<TodoDto>

    @Before
    fun tearUp() {

        todos = mutableListOf(
            TodoDto(
                NetworkSync.randomString(),
                "blabla",
                "Recopier les slides du prof",
                false,
                "appuyer sur ctrl+c et ctrl+v",
                "Hugo",
                "Yessine",
                "2023-12-05 23:59:59"
            ),
            TodoDto(
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
        }
    @Test
    fun createTaskTest() {
        runBlocking {
            var response = TodoRepository().createTask(todos[0].toJSONObject())
            assertEquals("\"OK\"",response.get("status").toString())

        }
    }
    @Test
    fun getTaskTest(){
        runBlocking {
            TodoRepository().createTask(todos[0].toJSONObject())
            assertTrue(TodoRepository().getTask().isNotEmpty())
        }
    }

    @Test
    fun closeTaskTest(){
        runBlocking {
            TodoRepository().createTask(todos[0].toJSONObject())
            var listTasks = TodoRepository().getTask()
            var response=TodoRepository().closeTask(listTasks[listTasks.size-1]._id)
            assertEquals("\"OK\"",response.get("status").toString())
    }
    }
    @Test
    fun openTaskTest(){
        runBlocking {
            TodoRepository().createTask(todos[1].toJSONObject())
            var listTasks = TodoRepository().getTask()
            var response=TodoRepository().openTask(listTasks[listTasks.size-1]._id)
            assertEquals("\"OK\"",response.get("status").toString())
        }
    }
    @Test
    fun deleteTaskTest(){
        runBlocking {
            TodoRepository().createTask(todos[1].toJSONObject())
            var listTasks = TodoRepository().getTask()
            var response=TodoRepository().deleteTask(listTasks[listTasks.size-1]._id)
            assertEquals("\"OK\"",response.get("status").toString())
        }
    }
}