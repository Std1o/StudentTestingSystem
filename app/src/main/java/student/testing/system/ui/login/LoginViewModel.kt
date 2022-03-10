package student.testing.system.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import student.testing.system.MainRepository
import student.testing.system.api.models.Token
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: MainRepository) : ViewModel(){

    fun getUser(userId : Int) : Flow<Token> {
        return flow {
            val data = repository.auth()
            data.body()?.let { emit(it) }
        }.flowOn(Dispatchers.IO)
    }
}