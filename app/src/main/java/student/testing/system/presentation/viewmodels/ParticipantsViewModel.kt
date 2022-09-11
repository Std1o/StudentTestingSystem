package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.data.MainRepository
import student.testing.system.domain.DataState
import student.testing.system.common.Utils
import student.testing.system.models.Participant
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    fun addModerator(
        courseId: Int,
        courseOwnerId: Int,
        moderatorId: Int
    ): StateFlow<DataState<List<Participant>>> {
        val stateFlow = MutableStateFlow<DataState<List<Participant>>>(DataState.Loading)
        viewModelScope.launch {
            repository.addModerator(courseId, courseOwnerId, moderatorId).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    fun deleteModerator(
        courseId: Int,
        courseOwnerId: Int,
        moderatorId: Int
    ): StateFlow<DataState<List<Participant>>> {
        val stateFlow = MutableStateFlow<DataState<List<Participant>>>(DataState.Loading)
        viewModelScope.launch {
            repository.deleteModerator(courseId, courseOwnerId, moderatorId).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }

    fun deleteParticipant(
        courseId: Int,
        courseOwnerId: Int,
        participantId: Int
    ): StateFlow<DataState<List<Participant>>> {
        val stateFlow = MutableStateFlow<DataState<List<Participant>>>(DataState.Loading)
        viewModelScope.launch {
            repository.deleteParticipant(courseId, courseOwnerId, participantId).collect {
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }
}