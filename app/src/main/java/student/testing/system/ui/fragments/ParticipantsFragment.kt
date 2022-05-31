package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.confirmAction
import student.testing.system.common.showIf
import student.testing.system.databinding.FragmentParticipantsBinding
import student.testing.system.models.CourseResponse
import student.testing.system.models.Participant
import student.testing.system.ui.adapters.ParticipantsAdapter
import student.testing.system.viewmodels.CourseSharedViewModel
import student.testing.system.viewmodels.ParticipantsViewModel


@AndroidEntryPoint
class ParticipantsFragment : Fragment() {

    private var _binding: FragmentParticipantsBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: ParticipantsAdapter
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

    private fun initRV(course: CourseResponse) {
        adapter = ParticipantsAdapter(course.participants, course.moderators, course.ownerId)
        if (course.ownerId == AccountSession.instance.userId) {
            adapter.listener = object : ParticipantsAdapter.MenuClickListener {
                override fun onMenuClick(view: View, participant: Participant) {
                    showPopup(view, participant, course)
                }
            }
        }
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }

    private fun showPopup(v: View, participant: Participant, course: CourseResponse) {
        val popup = PopupMenu(requireActivity(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.participant_context_menu, popup.menu)
        val titleRes =
            if (participant in adapter.moderators) R.string.remove_from_moderators
            else R.string.appoint_moderator
        popup.menu.getItem(0).title = getString(titleRes)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_moderator -> {
                    val title =
                        if (participant in adapter.moderators) R.string.remove_from_moderators_request
                        else R.string.appoint_moderator_request
                    confirmAction(title) { _, _ ->
                        if (participant in adapter.moderators) {
                            deleteModerator(course, participant.id)
                        } else {
                            addModerator(course, participant.id)
                        }
                    }
                }
                R.id.action_delete -> {
                    confirmAction(R.string.delete_request) { _, _ ->
                        deleteParticipant(course, participant.id)
                    }
                }
            }
            true
        }
    }

    private fun deleteParticipant(course: CourseResponse, participantId: Int) {
        lifecycleScope.launch {
            viewModel.deleteParticipant(course.id, course.ownerId, participantId).collect {
                binding.progressBar.showIf(it is DataState.Loading)
                if (it is DataState.Success) {
                    adapter.updateDataList(it.data)
                } else if (it is DataState.Error) {
                    binding.progressBar.visibility = View.GONE
                    val snackbar =
                        Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }
    }

    private fun deleteModerator(course: CourseResponse, participantId: Int) {
        lifecycleScope.launch {
            viewModel.deleteModerator(course.id, course.ownerId, participantId).collect {
                binding.progressBar.showIf(it is DataState.Loading)
                if (it is DataState.Success) {
                    adapter.updateModerators(it.data)
                } else if (it is DataState.Error) {
                    binding.progressBar.visibility = View.GONE
                    val snackbar =
                        Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }
    }

    private fun addModerator(course: CourseResponse, participantId: Int) {
        lifecycleScope.launch {
            viewModel.addModerator(course.id, course.ownerId, participantId).collect {
                binding.progressBar.showIf(it is DataState.Loading)
                if (it is DataState.Success) {
                    adapter.updateModerators(it.data)
                } else if (it is DataState.Error) {
                    binding.progressBar.visibility = View.GONE
                    val snackbar =
                        Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}