package student.testing.system.data.source.impl

import kotlinx.coroutines.flow.Flow
import student.testing.system.data.source.base.BaseWebSocketDataSourceImpl
import student.testing.system.data.source.interfaces.TestResultsRemoteDataSource
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.webSockets.WebsocketEvent
import javax.inject.Inject

class TestResultsRemoteDataSourceImpl @Inject constructor() : TestResultsRemoteDataSource,
    BaseWebSocketDataSourceImpl<TestResultsRequestParams>() {

    override fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): Flow<WebsocketEvent<ParticipantsResults>> =
        getData(
            url = "wss://testingsystem.ru/tests/ws/results/$testId?course_id=$courseId",
            params,
            ParticipantsResults::class.java
        )
}