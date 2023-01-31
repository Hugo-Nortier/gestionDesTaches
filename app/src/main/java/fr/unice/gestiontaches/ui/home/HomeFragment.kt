package fr.unice.gestiontaches.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fr.unice.gestiontaches.dal.room.TodoDatabase
import fr.unice.gestiontaches.databinding.FragmentHomeBinding
import fr.unice.gestiontaches.utils.NetworkSync
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var database: TodoDatabase
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val _connectstat = MutableLiveData<Boolean>().apply {
        //value = "This is notifications Fragment"
    }
    val connectstat: LiveData<Boolean> = _connectstat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val currentcontext = activity?.baseContext!!
        database = TodoDatabase.getInstance(currentcontext)

        val descriptionHome: TextView = binding.descHome
        val connectstatus: TextView = binding.connecte
        homeViewModel.text.observe(viewLifecycleOwner) {
            descriptionHome.text = it
        }

        connectstat.observe(viewLifecycleOwner){
            connectstatus.isVisible = it
        }

        lifecycleScope.launch {
            val connectivityManager = currentcontext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.let {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        _connectstat.postValue(false)
                    }
                    override fun onLost(network: Network) {
                       _connectstat.postValue(true)
                    }
                })
            }

            if (NetworkSync.checkOnline(activity?.baseContext!!))
                NetworkSync.syncDatabase(database)
            val tasks = database.todoDao().getAllTasks()

            descriptionHome.text = "Il y a actuellement un total de "+tasks.size+" tâches."
            connectstatus.isVisible = !NetworkSync.checkOnline(activity?.baseContext!!)
        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}