@file:OptIn(NotScreenState::class)

package student.testing.system.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import student.testing.system.FakeRepository
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.models.CourseResponse

@ExperimentalCoroutinesApi
class JoinCourseUseCaseTest {

    private val repository = FakeRepository()
    private val joinCourseUseCase = JoinCourseUseCase(repository)

    @Test
    fun `empty courseCode returns ValidationError`() = runTest {
        val actual = joinCourseUseCase("").first()
        assertTrue(actual is ValidatableOperationState.ValidationError)
    }

    @Test
    fun `failed response returns Error`() = runTest {
        val actual = joinCourseUseCase("QQQQQQ").toList()
        assertTrue(actual[0] is ValidatableOperationState.SuccessfulValidation)
        assertTrue(actual[1] is RequestState.Error)
    }

    @Test
    fun `success response returns CourseResponse`() = runTest {
        val actual = joinCourseUseCase("5TYHKW").toList()
        assertTrue(actual[0] is ValidatableOperationState.SuccessfulValidation)
        assertTrue(actual[1] is RequestState.Success)
        assertThat((actual[1] as RequestState.Success).data, instanceOf(CourseResponse::class.java))
    }
}