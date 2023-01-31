package fr.unice.gestiontaches.dal.repositories

import com.google.gson.JsonObject
import fr.unice.gestiontaches.models.Todo
import fr.unice.gestiontaches.ui.api.SimpleService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoRepository(
) {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://mbds.glitch.me/")
        .build()
    val apiService = retrofit.create(SimpleService::class.java)

    suspend fun getTask(): List<Todo> {
        return apiService.getTask()
    }
    suspend fun createTask(objet: JsonObject): JsonObject {
        return apiService.create(objet)
    }
    suspend fun closeTask(id:String):JsonObject {
        return apiService.closeTask(id)
    }
    suspend fun openTask(id:String): JsonObject {
        return apiService.openTask(id)
    }
    suspend fun deleteTask(id:String): JsonObject {
        return apiService.deleteTask(id)
    }

}