package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lilith.presentation.viewmodel.StatesViewModel
import stdio.lilith.core.delegates.StateFlowVar.Companion.stateFlowVar
import student.testing.system.data.api.KtorWebsocketClientImpl
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvents
import student.testing.system.presentation.ui.models.FiltersContainer
import student.testing.system.presentation.ui.models.contentState.ResultsContentState
import javax.inject.Inject


@Suppress("UNREACHABLE_CODE")
@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: TestsRepository,
) :
    StatesViewModel() {

    private val _contentState = MutableStateFlow(ResultsContentState())
    val contentState = _contentState.asStateFlow()
    private var contentStateVar by stateFlowVar(_contentState)

    private lateinit var test: Test
    var searchPrefix: String? = null
    val filtersContainer = FiltersContainer()

    private val client: KtorWebsocketClient by lazy {
        KtorWebsocketClientImpl(url = "wss://testingsystem.ru/tests/ws/results/${test.id}?course_id=${test.courseId}",
            object : WebsocketEvents {
                override fun onReceive(data: String) {
                    val dataJson = Gson().fromJson(
                        data,
                        ParticipantsResults::class.java
                    )
                    contentStateVar = contentStateVar.copy(
                        results = LoadableData.Success(dataJson)
                    )
                    if (filtersContainer.maxScore == 0) {
                        filtersContainer.maxScore = dataJson.maxScore
                        if (filtersContainer.maxScore == 0) {
                            filtersContainer.maxScore = 100 // this can happen if there are no results
                        }
                    }
                }

                override fun onConnected() {

                }

                override fun onDisconnected(reason: String) {
                    println("onDisconnected")
                }
            })
    }

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

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        contentStateVar = ResultsContentState(LoadableData.Loading())

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            /*loadData { repository.getResults(test.id, test.courseId, params) }.collect {
                contentStateVar = contentStateVar.copy(results = it)
                if (it is LoadableData.Success && filtersContainer.maxScore == 0) {
                    filtersContainer.maxScore = it.data.maxScore
                    if (filtersContainer.maxScore == 0) {
                        filtersContainer.maxScore = 100 // this can happen if there are no results
                    }
                }
            }*/
        }

        viewModelScope.launch {
            client.connect()
        }
        viewModelScope.launch {
            delay(1000)
            val jsonObject = Gson().toJson(params)
            client.send(jsonObject)
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            client.stop()
        }
        super.onCleared()
    }
}