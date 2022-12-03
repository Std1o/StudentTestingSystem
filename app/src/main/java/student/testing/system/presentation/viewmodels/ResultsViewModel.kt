package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.domain.MainRepository
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: MainRepository,
    val prefsUtils: PrefsUtils
) : BaseViewModel<ParticipantsResults>() {

    var showOnlyMaxResults: Boolean = false

    fun getResults(
        testId: Int,
        courseId: Int,
    ) {
        val params = TestResultsRequestParams(onlyMaxResult = showOnlyMaxResults)
        viewModelScope.launch {
            launchRequest(repository.getResults(testId, courseId, params))
        }
    }
}