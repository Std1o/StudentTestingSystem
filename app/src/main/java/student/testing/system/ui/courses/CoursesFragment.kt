package student.testing.system.ui.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.api.network.DataState
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.ui.dialogFragments.CourseAddingDialogFragment

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private val viewModel by viewModels<CoursesViewModel>()
    private lateinit var _binding: CoursesFragmentBinding
    private val binding get() = _binding
    companion object {
        private const val KEY_COURSE_ADDING = "teamsSettings"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoursesFragmentBinding.inflate(inflater, container, false)
        getCourses()
        binding.btnAdd.setOnClickListener() {
            CourseAddingDialogFragment
                .newInstance()
                .show(requireActivity().supportFragmentManager, KEY_COURSE_ADDING);
        }
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