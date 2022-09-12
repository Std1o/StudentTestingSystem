package student.testing.system.domain.getResult

import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import student.testing.system.FakeRepository

class GetResultUseCaseTest {

    private val repository = FakeRepository()
    private val getResultUseCase = spyk(GetResultUseCase(repository))

    @ExperimentalCoroutinesApi
    @Test
    fun `when 404 returns NoResult state`() = runTest {
        val expected = ResultState.NoResult
        val actual = getResultUseCase(1, 1).first()
        assertEquals(expected, actual)
    }
}