package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.domain.MainRepository
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.enums.OrderingType
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

@HiltViewModel
class ResultsSharedViewModel @Inject constructor(
    private val repository: MainRepository,
    val prefsUtils: PrefsUtils
) : BaseViewModel<ParticipantsResults>() {

    var maxScore: Int = 0
    var showOnlyMaxResults: Boolean = false
    var searchPrefix: String? = null
    var scoreEquals: Boolean = false
    var scoreEqualsValue: Float? = null
    var orderingType: OrderingType? = null

    fun getResults(
        testId: Int,
        courseId: Int,
    ) {
        val params = TestResultsRequestParams(
            onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
            scoreEquals = if (scoreEquals) scoreEqualsValue else null, ordering = orderingType
        )
        viewModelScope.launch {
            launchRequest(repository.getResults(testId, courseId, params))
        }
    }
}