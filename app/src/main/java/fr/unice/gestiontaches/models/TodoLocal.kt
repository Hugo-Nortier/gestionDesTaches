package fr.unice.gestiontaches.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoLocal (
    @PrimaryKey
    val localId: String,
    val _id: String?,
    val task: String?,
    var completed : Boolean?,
    val description: String?,
    val requested_by: String?,
    val assignee: String?,
    val due_date: String?,
    var supprime: Boolean = false,
    )

