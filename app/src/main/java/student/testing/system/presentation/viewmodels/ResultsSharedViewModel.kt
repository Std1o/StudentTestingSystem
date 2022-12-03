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
    var lowerBound: Float = 0f
    var upperBound: Float? = null
    var scoreEquals: Boolean = false
    var scoreEqualsValue: Float? = null
    var orderingType: OrderingType? = null

    fun getResults(
        testId: Int,
        courseId: Int,
    ) {
        val params = TestResultsRequestParams(
            onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
            scoreEquals = if (scoreEquals) scoreEqualsValue else null, ordering = orderingType,
            upperBound = upperBound, lowerBound = lowerBound
        )
        viewModelScope.launch {
            launchRequest(repository.getResults(testId, courseId, params))
        }
    }
}