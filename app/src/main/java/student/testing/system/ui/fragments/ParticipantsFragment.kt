package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.showIf
import student.testing.system.models.CourseResponse
import student.testing.system.databinding.FragmentParticipantsBinding
import student.testing.system.ui.adapters.CoursesAdapter
import student.testing.system.ui.adapters.ParticipantsAdapter
import student.testing.system.viewmodels.CourseSharedViewModel
import student.testing.system.viewmodels.LoginViewModel
import student.testing.system.viewmodels.ParticipantsViewModel

@AndroidEntryPoint
class ParticipantsFragment : Fragment() {

    private var _binding: FragmentParticipantsBinding? = null
    private val binding get() = _binding!!
    lateinit var participantsAdapter: ParticipantsAdapter
    private val viewModel by viewModels<ParticipantsViewModel>()
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
        if (course.ownerId == AccountSession.instance.userId) {
            participantsAdapter.listener = object : ParticipantsAdapter.LongClickListener {
                override fun onLongClick(participantId: Int) {
                    lifecycleScope.launch {
                        viewModel.addModerator(course.id, course.ownerId, participantId).collect {
                            binding.progressBar.showIf(it is DataState.Loading)
                            if (it is DataState.Success) {
                                participantsAdapter.updateModerators(it.data)
                            } else if (it is DataState.Error) {
                                binding.progressBar.visibility = View.GONE
                                val snackbar =
                                    Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                                snackbar.show()
                            }
                        }
                    }
                }
            }
        }
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = participantsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}