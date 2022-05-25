package student.testing.system.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import student.testing.system.R
import student.testing.system.common.showToast
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.databinding.TestCreationFragmentBinding
import student.testing.system.models.Question
import student.testing.system.ui.adapters.QuestionsAdapter

class TestCreationFragment : Fragment() {

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
    }
}