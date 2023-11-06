package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.launch
import student.testing.system.common.Utils
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.operationStates.protect
import student.testing.system.models.CourseResponse
import student.testing.system.models.Participant
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(private val repository: MainRepository) :
    StatesViewModel() {

    private var currentParticipant: Participant? = null

    fun isUserAnOwner(course: CourseResponse) = Utils.isUserAnOwner(course)

    fun addModerator(courseId: Int, moderatorId: Int) {
        viewModelScope.launch {
            executeOperationAndIgnoreData({
                repository.addModerator(courseId, moderatorId)
            }).protect()
        }
    }

    fun deleteModerator(courseId: Int, moderatorId: Int) {
        viewModelScope.launch {
            executeOperationAndIgnoreData({
                repository.deleteModerator(courseId, moderatorId)
            }).protect()
        }
    }

    fun deleteParticipant(courseId: Int, participantId: Int) {
        viewModelScope.launch {
            executeOperationAndIgnoreData({
                repository.deleteParticipant(courseId, participantId)
            }).protect()
        }
    }
}