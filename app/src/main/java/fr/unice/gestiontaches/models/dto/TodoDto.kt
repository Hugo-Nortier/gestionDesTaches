package fr.unice.gestiontaches.models.dto

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import fr.unice.gestiontaches.models.Todo
import fr.unice.gestiontaches.models.TodoLocal
import fr.unice.gestiontaches.utils.NetworkSync


class TodoDto(
    var localId: String,
    var _id: String?,
    var task: String?,
    var completed : Boolean?,
    var description: String?,
    var requested_by: String?,
    var assignee: String?,
    var due_date: String?
) {

    constructor(model: TodoLocal) : this( model.localId,model._id, model.task, model.completed, model.description, model.requested_by, model.assignee, model.due_date)
    constructor(model: Todo) : this( NetworkSync.randomString(),model._id, model.task, model.completed, model.description, model.requested_by, model.assignee, model.due_date)

    fun toLocalModel(): TodoLocal{
        return TodoLocal(
            this.localId,
            this._id,
            this.task,
            this.completed,
            this.description,
            this.requested_by,
            this.assignee,
            this.due_date
        )
    }

    fun toJSONObject(): JsonObject{
        val todo = Todo(
            this._id!!,
            this.task,
            this.completed,
            this.description,
            this.requested_by,
            this.assignee,
            this.due_date)
        val gson = Gson()
        val jsonParser = JsonParser()
        return jsonParser.parse(gson.toJson(todo)) as JsonObject
    }
}