package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lilith.presentation.viewmodel.StatesViewModel
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.common.Constants.BASE_URL
import student.testing.system.data.api.KtorWebsocketClient
import student.testing.system.data.api.KtorWebsocketClient.WebsocketEvents
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.presentation.ui.models.FiltersContainer
import student.testing.system.presentation.ui.models.contentState.ResultsContentState
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: TestsRepository
) :
    StatesViewModel() {

    private val _contentState = MutableStateFlow(ResultsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private lateinit var test: Test
    var searchPrefix: String? = null
    val filtersContainer = FiltersContainer()

    fun setInitialData(test: Test) {
        this.test = test
        getResults()
    }

    fun getResults() {
        val params = with(filtersContainer) {
            TestResultsRequestParams(
                onlyMaxResult = showOnlyMaxResults, searchPrefix = searchPrefix,
                upperBound = if (ratingRangeEnabled) upperBound else null,
                lowerBound = if (ratingRangeEnabled) lowerBound else null,
                scoreEquals = if (scoreEqualsEnabled) scoreEqualsValue else null,
                dateFrom = dateFrom, dateTo = dateTo, ordering = orderingType
            )
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            loadData { repository.getResults(test.id, test.courseId, params) }.collect {
                contentStateVar = contentStateVar.copy(results = it)
                if (it is LoadableData.Success && filtersContainer.maxScore == 0) {
                    filtersContainer.maxScore = it.data.maxScore
                    if (filtersContainer.maxScore == 0) {
                        filtersContainer.maxScore = 100 // this can happen if there are no results
                    }
                }
            }
        }

        // TODO relocate
        viewModelScope.launch {
            val ggg = KtorWebsocketClient(url = "wss://testingsystem.ru/tests/ws/results/58?course_id=1",
                    object : WebsocketEvents {
                        override fun onReceive(data: String) {
                            println("onReceive")
                        }

                        override fun onConnected() {
                            println("onConnected")
                        }

                        override fun onDisconnected(reason: String) {
                            println("onDisconnected")
                        }
                    })
            ggg.connect()
        }
    }
}