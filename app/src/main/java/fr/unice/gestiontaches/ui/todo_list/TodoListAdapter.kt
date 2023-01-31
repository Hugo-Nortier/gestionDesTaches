package fr.unice.gestiontaches.ui.todo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.unice.gestiontaches.databinding.ItemListTodoBinding
import fr.unice.gestiontaches.models.TodoLocal
import fr.unice.gestiontaches.utils.TodoDiffCallback

class TodoListAdapter(  // FOR CALLBACK ---
    private val callback: Listener
) : RecyclerView.Adapter<ListTodoViewHolder>() {
    // FOR DATA ---
    private var todos: List<TodoLocal> = ArrayList()
    private var position:Int=0
    interface Listener {
        fun onClickDelete(todo: TodoLocal)
        fun onClickUpdateStatut(todo:TodoLocal)
        fun onClickShareTask(todo:TodoLocal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTodoViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding= ItemListTodoBinding.inflate(inflater,parent,false)
        return  ListTodoViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ListTodoViewHolder, position: Int) {
        holder.bind(todos[position], callback)
        this.position=holder.adapterPosition
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    // PUBLIC API ---
    fun updateList(newList: List<TodoLocal>) {
        val mutableList = newList.toMutableList()
        val indexesDel = mutableListOf<Int>()
        mutableList.forEachIndexed {index,it->
            if(it.supprime)
                indexesDel.add(index)
        }
        var counter = 0
        indexesDel.forEach{
            mutableList.removeAt(it-counter)
            counter++
        }
        val diffResult = DiffUtil.calculateDiff(TodoDiffCallback(mutableList.toList(), todos))
        todos = mutableList.toList()
        diffResult.dispatchUpdatesTo(this)
    }
    fun getPosition():Int{
        return this.position
    }
}