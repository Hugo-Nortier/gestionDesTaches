package fr.unice.gestiontaches.ui.todo_list
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import fr.unice.gestiontaches.R
import fr.unice.gestiontaches.databinding.ItemListTodoBinding
import fr.unice.gestiontaches.models.TodoLocal

class ListTodoViewHolder(private val binding: ItemListTodoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    // FOR DESIGN ---

    fun bind(todo: TodoLocal, callback: TodoListAdapter.Listener) {
        binding.task.text = todo.task
        binding.assignee.text = "assigné à: " + todo.assignee
        binding.description.text = todo.description
        binding.requester.text = "demandé par: " + todo.requested_by
        binding.duedate.text = todo.due_date
        if (todo.completed == true){
            binding.root.setBackgroundColor(Color.parseColor("#90ee90"))
            binding.completeBtn.setImageResource(R.drawable.ic_baseline_complete_todo_24)
        } else {
            binding.root.setBackgroundColor(Color.WHITE)
            binding.completeBtn.setImageResource(R.drawable.ic_baseline_uncomplete_todo_24)
        }
        binding.deleteBtn.setOnClickListener {
            callback.onClickDelete(todo)
        }
        binding.completeBtn.setOnClickListener {
            callback.onClickUpdateStatut(todo)
        }
        binding.shareBtn.setOnClickListener{
            callback.onClickShareTask(todo)
        }
    }
}