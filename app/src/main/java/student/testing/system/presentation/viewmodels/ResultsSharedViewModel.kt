package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.domain.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.enums.OrderingType
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ResultsSharedViewModel @Inject constructor(
    private val repository: MainRepository,
    val prefsUtils: PrefsUtils
) : BaseViewModel<ParticipantsResults>() {

    var maxScore: Int by Delegates.observable(0) {
            _, _, new ->
        upperBound = new.toFloat()
    }
    var showOnlyMaxResults: Boolean = false
    var searchPrefix: String? = null
    var ratingRangeEnabled = true
    var lowerBound: Float = 0f
    var upperBound: Float? = null
    var scoreEqualsEnabled: Boolean = false
    var scoreEqualsValue: Float? = null
    var dateFrom: String? = null
    var dateTo: String? = null
    var orderingType: OrderingType? = null

    fun getResults(
        testId: Int,
        courseId: Int,
    ) {
        val params = TestResultsRequestParams(
            onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
            upperBound = if (ratingRangeEnabled) upperBound else null,
            lowerBound = if (ratingRangeEnabled) lowerBound else null,
            scoreEquals = if (scoreEqualsEnabled) scoreEqualsValue else null,
            dateFrom = dateFrom, dateTo = dateTo, ordering = orderingType
        )
        viewModelScope.launch {
            launchRequest(repository.getResults(testId, courseId, params)) {
                if (it is DataState.Success && maxScore == 0) {
                    maxScore = it.data.maxScore
                    if (maxScore == 0) maxScore = 100 // this can happen if there are no results
                }
            }
        }
    }
}