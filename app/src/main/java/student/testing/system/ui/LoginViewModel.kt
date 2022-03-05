package student.testing.system.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import student.testing.system.MainRepository
import student.testing.system.api.models.CreateOperationReq
import student.testing.system.models.User
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: MainRepository) : ViewModel(){

    fun getUser(userId : Int) : LiveData<CreateOperationReq> {
        return liveData {
            val data = repository.auth()
            data.body()?.let { emit(it) }
        }
    }
}