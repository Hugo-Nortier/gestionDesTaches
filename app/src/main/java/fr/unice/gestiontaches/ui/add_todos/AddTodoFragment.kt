package fr.unice.gestiontaches.ui.add_todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fr.unice.gestiontaches.dal.room.TodoDatabase
import fr.unice.gestiontaches.databinding.AddTodoBinding
import fr.unice.gestiontaches.models.dto.TodoDto
import fr.unice.gestiontaches.utils.DateInputMask
import fr.unice.gestiontaches.utils.NetworkSync
import kotlinx.coroutines.launch

class AddTodoFragment : Fragment() {

    private var _binding: AddTodoBinding? = null
    lateinit var database: TodoDatabase
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var isAllFieldsChecked = false

    private lateinit var nomTache:TextView
    private lateinit var assignee:TextView
    private lateinit var dueDate:EditText
    private lateinit var descripton:TextView
    private lateinit var requestedBy:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addViewModel =
            ViewModelProvider(this).get(AddTodoViewModel::class.java)
        _binding = AddTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = TodoDatabase.getInstance(activity?.baseContext!!)

        nomTache = binding.nomTacheInput
        addViewModel.text.observe(viewLifecycleOwner) {
            nomTache.text = it
        }
        assignee=binding.assigneeInput
        dueDate=binding.dueDateInput
        DateInputMask(dueDate).listen()
        descripton=binding.descTodoInput
        requestedBy=binding.requestedByInput
        val buttonAdd: Button =binding.createBtn

        buttonAdd.setOnClickListener{
            lifecycleScope.launch {
                isAllFieldsChecked = checkAllFields()
                if (isAllFieldsChecked)
                    addTask(
                        TodoDto(
                            NetworkSync.randomString(),
                            "",
                            nomTache.text.toString(),
                            false,
                            descripton.text.toString(),
                            requestedBy.text.toString(),
                            assignee.text.toString(),
                            dueDate.text.toString()
                        )
                    )
                else  Toast.makeText(activity,"problèmes de champs", Toast.LENGTH_SHORT).show()

            }
        }
        return root
    }

    private fun checkAllFields(): Boolean {
        if (nomTache.text.toString()=="") {
            nomTache.error = "titre est requis"
            return false
        }
        if (assignee.text.toString()=="") {
            assignee.error = "assigné à est requis"
            return false
        }
        if (dueDate.text.toString()==""||dueDate.text.length!=10) {
            dueDate.error = "date limite est requise - saisir dd/mm/yyyy"
            return false
        }
        if (requestedBy.text.toString()=="") {
            requestedBy.error = "demandé par est requis"
            return false
        }
        if (descripton.text.toString()=="") {
            descripton.error = "description est requise"
            return false
        }
        if(dueDate.text.substring(6,10).toInt()<2022){
            dueDate.error = "Todo dans le passé - saisir dd/mm/yyyy"
            return false
        }
        if(dueDate.text.substring(0,2).toInt()>31||dueDate.text.substring(3,5).toInt()>12){
            dueDate.error = "date invalide - saisir dd/mm/yyyy"
            return false
        }
        return true
    }

    private fun clearFields(){
        nomTache.text=""
        descripton.text=""
        requestedBy.text=""
        assignee.text=""
        dueDate.text.clear()
        nomTache.clearFocus()
        descripton.clearFocus()
        requestedBy.clearFocus()
        assignee.clearFocus()
        dueDate.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun addTask(todo: TodoDto){
        lifecycleScope.launch {
            database.todoDao().insertTask(todo.toLocalModel())
            if(NetworkSync.checkOnline(activity?.baseContext!!)){
                NetworkSync.syncDatabase(database)
            }
            Toast.makeText(activity,"Tâche ajoutée", Toast.LENGTH_SHORT).show()
            clearFields()
        }
    }
}