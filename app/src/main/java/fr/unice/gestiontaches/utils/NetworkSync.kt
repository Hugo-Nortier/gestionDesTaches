package fr.unice.gestiontaches.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import fr.unice.gestiontaches.dal.repositories.TodoRepository
import fr.unice.gestiontaches.dal.room.TodoDatabase
import fr.unice.gestiontaches.models.dto.TodoDto
import java.util.*


class NetworkSync {
    companion object {
        fun checkOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }

            Log.d("Internet", "No internet connection")
            return false
        }

        suspend fun syncDatabase(database: TodoDatabase) {
            val localtodos = database.todoDao().getAllTasks()
            for (todo in localtodos) {
                if (todo.supprime && todo._id != "")
                    TodoRepository().deleteTask(todo._id!!)
                else if (todo._id == "")
                    TodoRepository().createTask(TodoDto(todo).toJSONObject())
            }
            database.todoDao().deleteAllTasks()
            val todos = TodoRepository().getTask()
            for (todo in todos)
                database.todoDao().insertTask(TodoDto(todo).toLocalModel())
        }

        fun randomString(): String {
            return UUID.randomUUID().toString()
        }
    }
}