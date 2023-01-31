package fr.unice.gestiontaches.ui.api;

import com.google.gson.JsonObject
import fr.unice.gestiontaches.models.Todo
import retrofit2.http.*

interface SimpleService {
    @POST("/todos")
    suspend fun create(@Body post : JsonObject): JsonObject

    @GET("/todos")
    suspend fun getTask(): List<Todo>

    @PUT("/todos/{idTask}/status/1")
    suspend fun closeTask(@Path("idTask")idTask:String): JsonObject

    @PUT("/todos/{idTask}/status/0")
    suspend fun openTask(@Path("idTask")idTask:String): JsonObject

    @DELETE("/todos/{idTask}")  // becomes post/search?filter=query
    suspend fun deleteTask(@Path("idTask")idTask:String): JsonObject
}
