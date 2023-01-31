package fr.unice.gestiontaches.ui.add_todos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTodoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}