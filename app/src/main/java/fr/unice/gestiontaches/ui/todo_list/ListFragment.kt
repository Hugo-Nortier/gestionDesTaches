package fr.unice.gestiontaches.ui.todo_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import fr.unice.gestiontaches.dal.repositories.TodoRepository
import fr.unice.gestiontaches.dal.room.TodoDatabase
import fr.unice.gestiontaches.databinding.FragmentListTodoBinding
import fr.unice.gestiontaches.models.TodoLocal
import fr.unice.gestiontaches.utils.NetworkSync
import kotlinx.coroutines.launch


class ListFragment : Fragment(), TodoListAdapter.Listener {

    private var _binding: FragmentListTodoBinding? = null
    private lateinit var adapter: TodoListAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var database: TodoDatabase

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        configureRecyclerView()
        recyclerView.run {
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureRecyclerView() {
        recyclerView = _binding!!.activityListTodoRv
        adapter = TodoListAdapter(this)
        recyclerView.adapter = adapter
        database = TodoDatabase.getInstance(recyclerView.context)
        lifecycleScope.launch {
            loadData()
        }
    }


    private suspend fun loadData() {
        if (NetworkSync.checkOnline(recyclerView.context))
            NetworkSync.syncDatabase(database)
        adapter.updateList(database.todoDao().getAllTasks())

    }

    override fun onClickDelete(todo: TodoLocal) {
        Log.d(ListFragment::class.java.name, "Todo tries to delete a item.")
        lifecycleScope.launch {
            todo.supprime = true
            database.todoDao().updateTask(todo)
            loadData()
            recyclerView.scrollToPosition(adapter.getPosition()-1);
        }
    }

    override fun onClickUpdateStatut(todo: TodoLocal) {
        Log.d(ListFragment::class.java.name, "Todo tries to update a item.")
        lifecycleScope.launch {
            if(todo.completed != true) Toast.makeText(activity,"Todo completé!",Toast.LENGTH_SHORT).show();
            else Toast.makeText(activity,"Todo n'est plus completé :'(!",Toast.LENGTH_SHORT).show();
            if(NetworkSync.checkOnline(recyclerView.context)){
                if (todo.completed == true) {
                    TodoRepository().openTask(todo._id!!)
                } else TodoRepository().closeTask(todo._id!!)
            }
            else {
                todo.completed = todo.completed != true
                database.todoDao().updateTask(todo)
            }
            loadData()
            recyclerView.scrollToPosition(adapter.getPosition());

        }
    }
    override fun onClickShareTask(todo: TodoLocal) {
        Log.d(ListFragment::class.java.name, "Todo tries to update a item.")
        lifecycleScope.launch {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val envoi=todo.requested_by+" a assigné à "+todo.assignee+" la tâche \""+todo.task+"\" qui consiste à "+todo.description+" dont la date limite est le "+todo.due_date
            intent.putExtra(Intent.EXTRA_TEXT, envoi)
            startActivity(Intent.createChooser(intent, "Voici ma tâche"))
        }
    }
}