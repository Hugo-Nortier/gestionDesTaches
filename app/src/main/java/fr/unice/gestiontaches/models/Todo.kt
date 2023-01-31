package fr.unice.gestiontaches.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "todos")
data class Todo (
    @SerializedName("_id")
    @PrimaryKey
    val _id: String,
    @SerializedName("task")
    val task: String?,
    @SerializedName("completed")
    val completed : Boolean?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("requested_by")
    val requested_by: String?,
    @SerializedName("assignee")
    val assignee: String?,
    @SerializedName("due_date")
    val due_date: String?)



