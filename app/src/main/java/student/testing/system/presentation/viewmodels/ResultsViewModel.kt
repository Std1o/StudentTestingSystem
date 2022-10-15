package student.testing.system.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.domain.MainRepository
import student.testing.system.domain.DataState
import student.testing.system.common.Utils
import student.testing.system.models.ParticipantsResults
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: MainRepository,
    val prefsUtils: PrefsUtils
) : ViewModel() {

    val stateFlow = MutableStateFlow<DataState<ParticipantsResults>>(DataState.Initial)

    fun getResults(
        testId: Int,
        courseId: Int,
        showOnlyMaxResults: Boolean = false
    ) = viewModelScope.launch {
        stateFlow.emit(DataState.Loading)
        repository.getResults(testId, courseId, showOnlyMaxResults).collect {
            stateFlow.emit(it)
        }
    }
}