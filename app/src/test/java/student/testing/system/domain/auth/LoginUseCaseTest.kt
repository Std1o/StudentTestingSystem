package student.testing.system.domain.auth

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import student.testing.system.R
import student.testing.system.FakeRepository
import student.testing.system.domain.states.AuthState
import student.testing.system.domain.states.OperationState


@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    private val repository = FakeRepository()
    private val prefsUtils = mockk<PrefsUtils>(relaxed = true)
    private val validateAuthDataUseCase = ValidateAuthDataUseCase()
    private val loginUseCase = LoginUseCase(repository, prefsUtils, validateAuthDataUseCase)

    @Test
    fun `empty email returns EmailError`() = runTest {
        val expected = AuthState.EmailError(R.string.error_empty_field)
        val actual = loginUseCase.invoke("", "")
        assertEquals(expected, actual)
    }

    @Test
    fun `invalid E-mail format returns EmailError`() = runTest {
        val expected = AuthState.EmailError(R.string.error_invalid_email)
        val actual = loginUseCase.invoke("someEmail", "")
        assertEquals(expected, actual)
    }

    @Test
    fun `empty password returns PasswordError`() = runTest {
        val expected = AuthState.PasswordError(R.string.error_empty_field)
        val actual = loginUseCase.invoke("test@mail.ru", "")
        assertEquals(expected, actual)
    }

    @Test
    fun `success auth returns PrivateUser`() = runBlocking {
        val actual = loginUseCase.invoke("test@mail.ru", "pass")
        assertTrue(actual is OperationState.Success)
        assertThat((actual as OperationState.Success).data, instanceOf(PrivateUser::class.java))
    }

    @Test
    fun `failed auth returns Error`() = runBlocking {
        val expected = OperationState.Error("Incorrect username or password")
        val actual = loginUseCase.invoke("other@mail.ru", "pass")
        assertEquals(expected, actual)
    }
}