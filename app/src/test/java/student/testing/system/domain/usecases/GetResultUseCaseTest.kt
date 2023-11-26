package student.testing.system.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import student.testing.system.data.repository.FakeTestsRepository
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.ResultState
import student.testing.system.domain.models.TestResult


@ExperimentalCoroutinesApi
class GetResultUseCaseTest {

    private val repository = FakeTestsRepository()
    private val getResultUseCase = GetResultUseCase(repository)

    @Test
    fun `when 404 returns NoResult state`() = runTest {
        val expected = ResultState.NoResult
        val actual = getResultUseCase(1, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `all errors except 404 returns Error state`() = runTest {
        val actual = getResultUseCase(-1, 1)
        assertTrue(actual is OperationState.Error)
    }

    @Test
    fun `success response returns TestResult`() = runTest {
        val actual = getResultUseCase(13, 1)
        assertTrue(actual is OperationState.Success)
        assertThat((actual as OperationState.Success).data, instanceOf(TestResult::class.java))
    }
}