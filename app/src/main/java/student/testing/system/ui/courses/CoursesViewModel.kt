package student.testing.system.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import student.testing.system.api.models.Token
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.network.DataState
import student.testing.system.api.network.MainRepository
import student.testing.system.common.Utils
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun getCourses(): StateFlow<DataState<List<CourseResponse>>> {
        val stateFlow = MutableStateFlow<DataState<List<CourseResponse>>>(DataState.Loading)
        viewModelScope.launch {
            repository.getCourses().catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(it.body()!!))
                } else {
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }
}