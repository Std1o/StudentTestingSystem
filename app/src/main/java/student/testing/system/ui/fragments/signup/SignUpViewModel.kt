package student.testing.system.ui.fragments.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.api.network.MainRepository
import student.testing.system.api.models.Token
import student.testing.system.api.network.DataState
import student.testing.system.common.Utils
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val repository: MainRepository) : ViewModel(){

    fun signUp(email: String, username: String, password: String): StateFlow<DataState<Token>> {
        val stateFlow = MutableStateFlow<DataState<Token>>(DataState.Loading)
        viewModelScope.launch {
            repository.signUp(email, username, password).catch {
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