package student.testing.system.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.MainRepository
import student.testing.system.api.DataState
import student.testing.system.api.models.Token
import student.testing.system.common.Utils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {

    fun auth(): StateFlow<DataState<Token>> {
        val stateFlow = MutableStateFlow<DataState<Token>>(DataState.Loading)
        viewModelScope.launch {
            repository.auth().catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                print(it.isSuccessful)
                if (it.isSuccessful) {
                    stateFlow.emit(DataState.Success(it.body()!!))
                } else {
                    if (it.errorBody() == null) {
                        stateFlow.emit(DataState.Error("Unknown error"))
                    } else {
                        stateFlow.emit(DataState.Error(Utils.encodeErrorCode(it.errorBody()!!)))
                    }
                }
            }
        }
        return stateFlow
    }
}