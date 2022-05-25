package student.testing.system.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.formatToString
import student.testing.system.databinding.TestCreationFragmentBinding
import student.testing.system.models.Question
import student.testing.system.models.Test
import student.testing.system.ui.adapters.QuestionsAdapter
import student.testing.system.viewmodels.CourseSharedViewModel
import student.testing.system.viewmodels.TestsViewModel
import java.util.*

@AndroidEntryPoint
class TestCreationFragment : Fragment() {

    private val sharedViewModel: CourseSharedViewModel by activityViewModels()
    private val viewModel by viewModels<TestsViewModel>()
    private lateinit var _binding: TestCreationFragmentBinding
    private val binding get() = _binding
    lateinit var adapter: QuestionsAdapter

    companion object {
        const val ARG_QUESTION = "question"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TestCreationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = QuestionsAdapter(arrayListOf())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        binding.btnAdd.setOnClickListener() {
            findNavController().navigate(R.id.action_testCreationFragment_to_questionCreationFragment)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Question>(
            ARG_QUESTION
        )?.observe(viewLifecycleOwner) {
            adapter.addItem(it)
        }
        binding.btnPublish.setOnClickListener {
            lifecycleScope.launch {
                sharedViewModel.courseFlow.distinctUntilChanged().collect {
                    createTest(it.id)
                }
            }
        }
    }

    private fun createTest(courseId: Int) {
        lifecycleScope.launch {
            viewModel.createTest(
                courseId,
                binding.etName.text.toString(),
                Date().formatToString("yyyy-MM-dd")!!,
                adapter.dataList
            ).collect {
                when (it) {
                    is DataState.Loading -> {
                        //binding.progressBar.visibility = View.VISIBLE
                    }
                    is DataState.Error -> {
                        //binding.progressBar.visibility = View.GONE
                        val snackbar =
                            Snackbar.make(
                                binding.root,
                                it.exception,
                                Snackbar.LENGTH_SHORT
                            )
                        snackbar.show()
                    }
                    is DataState.Success -> {
                        //binding.progressBar.visibility = View.GONE
                        //testsAdapter.setDataList(it.data as MutableList<Test>)
                    }
                }
            }
        }
    }
}