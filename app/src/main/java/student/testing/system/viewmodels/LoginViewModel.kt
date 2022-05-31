package student.testing.system.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.api.network.MainRepository
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.Utils
import student.testing.system.models.PrivateUser
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun auth(email: String, password: String): StateFlow<DataState<PrivateUser>> {
        val stateFlow = MutableStateFlow<DataState<PrivateUser>>(DataState.Loading)
        viewModelScope.launch {
            repository.auth(email, password).catch {
                stateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    val privateUser = it.body()!!
                    AccountSession.instance.token = privateUser.token
                    AccountSession.instance.userId = privateUser.id
                    stateFlow.emit(DataState.Success(privateUser))
                } else {
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    stateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
        return stateFlow
    }
}