package student.testing.system.ui.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import student.testing.system.api.network.MainRepository
import student.testing.system.api.models.Token
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val repository: MainRepository) : ViewModel(){

    fun signUp(email: String, username: String, password: String) : Flow<Token> {
        return flow {
            val data = repository.signUp(email, username, password)
            data.body()?.let { emit(it) }
        }.flowOn(Dispatchers.IO)
    }
}