package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import student.testing.system.models.CourseResponse
import student.testing.system.databinding.FragmentParticipantsBinding
import student.testing.system.ui.adapters.ParticipantsAdapter
import student.testing.system.viewmodels.CourseSharedViewModel

class ParticipantsFragment : Fragment() {

    private var _binding: FragmentParticipantsBinding? = null
    private val binding get() = _binding!!
    lateinit var participantsAdapter: ParticipantsAdapter
    private val sharedViewModel: CourseSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParticipantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            sharedViewModel.courseFlow.distinctUntilChanged().collect {
                initRV(it)
            }
        }

    }

    private fun initRV(course : CourseResponse) {
        participantsAdapter = ParticipantsAdapter(course.participants,course.moderators, course.ownerId)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = participantsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}