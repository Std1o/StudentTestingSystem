package student.testing.system.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.api.network.MainRepository
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.Utils
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.PrivateUser
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
        courseOwnerId: Int,
        showOnlyMaxResults: Boolean = false
    ) = viewModelScope.launch {
        stateFlow.emit(DataState.Loading)
        repository.getResults(testId, courseId, courseOwnerId, showOnlyMaxResults).catch {
            stateFlow.emit(DataState.Error(it.message ?: " "))
        }.collect {
            if (it.isSuccessful) {
                stateFlow.emit(DataState.Success(it.body()!!))
            } else {
                Log.d("errorCode", it.code().toString())
                val errorMessage = Utils.encodeErrorCode(it.errorBody())
                stateFlow.emit(DataState.Error(errorMessage))
            }
        }
    }
}