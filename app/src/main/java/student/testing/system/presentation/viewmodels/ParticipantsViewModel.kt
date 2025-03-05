package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import lilith.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.launch
import student.testing.system.common.Utils
import student.testing.system.domain.models.Course
import student.testing.system.domain.repository.CourseManagementRepository
import student.testing.system.domain.states.operationStates.protect
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(private val repository: CourseManagementRepository) :
    StatesViewModel() {

    fun isUserAnOwner(course: Course) = Utils.isUserAnOwner(course)

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