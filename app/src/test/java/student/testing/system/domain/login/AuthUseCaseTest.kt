package student.testing.system.domain.login

import android.util.Log
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import student.testing.system.domain.MainRepository
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import student.testing.system.R
import org.junit.Assert.*
import org.hamcrest.MatcherAssert.assertThat
import student.testing.system.models.Participant
import student.testing.system.models.Question


@ExperimentalCoroutinesApi
class AuthUseCaseTest {

    private val repository = mockk<MainRepository>()
    private val prefsUtils = mockk<PrefsUtils>()
    private val authUseCase = spyk(AuthUseCase(repository, prefsUtils))

    @Test
    fun `empty email returns EmailError`() = runTest {
        val expected = LoginState.EmailError(R.string.error_empty_field)
        authUseCase.invoke("", "").collect { actual ->
            assertEquals(expected, actual)
        }
    }

    @Test
    //it doesn't work correctly
    fun `success auth returns PrivateUser`() = runBlocking {
        coEvery { authUseCase.invoke("test@mail.ru", "pass") } coAnswers {
            flow { LoginState.Success(PrivateUser(1, "Ivan", "test@mail.ru", "some_token")) }
        }
        authUseCase.invoke("test@mail.ru", "pass").collect { actual ->
            assertThat(actual, instanceOf(Question::class.java))
        }
    }
}