package student.testing.system.presentation.ui.fragments

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import student.testing.system.R
import student.testing.system.common.*
import student.testing.system.databinding.FragmentParticipantsBinding
import student.testing.system.models.CourseResponse
import student.testing.system.models.Participant
import student.testing.system.presentation.ui.adapters.ParticipantsAdapter
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.ParticipantsViewModel


@AndroidEntryPoint
class ParticipantsFragment : Fragment(R.layout.fragment_participants) {

    private val binding by viewBinding(FragmentParticipantsBinding::bind)
    lateinit var adapter: ParticipantsAdapter
    private val viewModel by viewModels<ParticipantsViewModel>()
    private val sharedViewModel: CourseSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.courseFlow.distinctUntilChanged().onEach {
            initRV(it)
        }.launchWhenStartedCollect(lifecycleScope)
    }

    private fun initRV(course: CourseResponse) {
        adapter = ParticipantsAdapter(course.participants, course.moderators, course.ownerId)
        if (course.ownerId == AccountSession.instance.userId) {
            adapter.listener = { view, participant ->
                showPopup(view, participant, course)
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
                    val message =
                        if (participant in adapter.moderators) R.string.remove_from_moderators_request
                        else R.string.appoint_moderator_request
                    confirmAction(message) { _, _ ->
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
        viewModel.deleteParticipant(course.id, course.ownerId, participantId)
            .subscribeInUI(this, binding.progressBar) {
                course.participants = it
                sharedViewModel.setCourse(course)
                adapter.updateDataList(it)
            }
    }

    private fun deleteModerator(course: CourseResponse, participantId: Int) {
        viewModel.deleteModerator(course.id, course.ownerId, participantId)
            .subscribeInUI(this, binding.progressBar) {
                course.moderators = it
                sharedViewModel.setCourse(course)
                adapter.updateModerators(it)
            }
    }

    private fun addModerator(course: CourseResponse, participantId: Int) {
        viewModel.addModerator(course.id, course.ownerId, participantId)
            .subscribeInUI(this, binding.progressBar) {
                course.moderators = it
                sharedViewModel.setCourse(course)
                adapter.updateModerators(it)
            }
    }
}