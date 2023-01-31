package fr.unice.gestiontaches.utils
import androidx.recyclerview.widget.DiffUtil
import fr.unice.gestiontaches.models.TodoLocal

class TodoDiffCallback(private val newTodos: List<TodoLocal>, private val oldTodos: List<TodoLocal>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTodos.size
    }

    override fun getNewListSize(): Int {
        return newTodos.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTodos[oldItemPosition]._id === newTodos[newItemPosition]._id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTodos[oldItemPosition] == newTodos[newItemPosition]
    }
}