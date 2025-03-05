package student.testing.system.data.source.interfaces

import kotlinx.coroutines.flow.Flow
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.webSockets.WebsocketEvent

interface TestResultsRemoteDataSource {
    fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): Flow<WebsocketEvent<ParticipantsResults>>
}