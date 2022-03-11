package student.testing.system.ui.courses

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.databinding.LoginFragmentBinding
import student.testing.system.ui.MainActivity
import student.testing.system.ui.login.LoginViewModel

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private val viewModel by viewModels<CoursesViewModel>()
    private lateinit var _binding: CoursesFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoursesFragmentBinding.inflate(inflater, container, false)
        getCourses()
        return binding.root
    }

    private fun getCourses() {
        lifecycleScope.launch {
            viewModel.getCourses().collect {
                when (it) {
                    is DataState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is DataState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        val snackbar = Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("course", it.data.toString())
                    }
                }
            }
        }
    }
}