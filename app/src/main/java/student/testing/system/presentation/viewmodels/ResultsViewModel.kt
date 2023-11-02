package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import stdio.godofappstates.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.domain.MainRepository
import student.testing.system.models.Test
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.enums.OrderingType
import student.testing.system.presentation.ui.models.contentState.ResultsContentState
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ResultsViewModel @Inject constructor(private val repository: MainRepository) :
    StatesViewModel() {

    private val _contentState = MutableStateFlow(ResultsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private lateinit var test: Test

    var maxScore: Int by Delegates.observable(0) { _, _, new ->
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

    fun setInitialData(test: Test) {
        this.test = test
        getResults()
    }

    fun getResults() {
        val params = TestResultsRequestParams(
            onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
            upperBound = if (ratingRangeEnabled) upperBound else null,
            lowerBound = if (ratingRangeEnabled) lowerBound else null,
            scoreEquals = if (scoreEqualsEnabled) scoreEqualsValue else null,
            dateFrom = dateFrom, dateTo = dateTo, ordering = orderingType
        )

        viewModelScope.launch {
            loadData { repository.getResults(test.id, test.courseId, params) }.collect {
                contentStateVar = contentStateVar.copy(results = it)
            }
        }
    }
}