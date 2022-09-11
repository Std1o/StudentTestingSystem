package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import student.testing.system.models.Question
import javax.inject.Inject

@HiltViewModel
class TestCreationViewModel @Inject constructor() : ViewModel() {

    private val questions: ArrayList<Question> = arrayListOf()
    val questionsFlow = MutableSharedFlow<ArrayList<Question>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun addQuestion(question: Question) {
        questions += question
        viewModelScope.launch {
            questionsFlow.tryEmit(questions)
        }
    }
}