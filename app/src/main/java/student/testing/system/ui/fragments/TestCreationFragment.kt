package student.testing.system.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import student.testing.system.R
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.databinding.TestCreationFragmentBinding

class TestCreationFragment : Fragment() {

    private lateinit var _binding: TestCreationFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TestCreationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener() {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_testCreationFragment_to_questionCreationFragment)
        }
    }
}