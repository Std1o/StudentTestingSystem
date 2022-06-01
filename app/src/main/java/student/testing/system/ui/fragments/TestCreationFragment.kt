package student.testing.system.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.*
import student.testing.system.databinding.TestCreationFragmentBinding
import student.testing.system.ui.adapters.QuestionsAdapter
import student.testing.system.viewmodels.CourseSharedViewModel
import student.testing.system.viewmodels.TestCreationViewModel
import student.testing.system.viewmodels.TestsViewModel
import java.util.*

@AndroidEntryPoint
class TestCreationFragment : Fragment(R.layout.test_creation_fragment) {

    private val sharedViewModel: CourseSharedViewModel by activityViewModels()
    private val testsViewModel by viewModels<TestsViewModel>()
    private val viewModel: TestCreationViewModel by activityViewModels()
    private val binding by viewBinding(TestCreationFragmentBinding::bind)
    lateinit var adapter: QuestionsAdapter

    companion object {
        const val ARG_TEST = "test"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = QuestionsAdapter(arrayListOf())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        binding.btnAdd.setOnClickListener() {
            findNavController().navigate(R.id.action_testCreationFragment_to_questionCreationFragment)
        }
        lifecycleScope.launch {
            viewModel.questionsFlow.distinctUntilChanged().collect {
                adapter.setDataList(it)
            }
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
        testsViewModel.createTest(
            courseId,
            binding.etName.text.toString(),
            Date().formatToString("yyyy-MM-dd")!!,
            adapter.dataList
        ).subscribeInUI(this, binding.progressBar) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                ARG_TEST, it)
            requireActivity().onBackPressed()
        }
    }
}