package student.testing.system.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.api.network.MainRepository
import student.testing.system.api.network.DataState
import student.testing.system.models.Token
import student.testing.system.common.Utils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun auth(email: String, password: String): StateFlow<DataState<Token>> {
        val stateFlow = MutableStateFlow<DataState<Token>>(DataState.Loading)
        viewModelScope.launch {
            repository.auth(email, password).catch {
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