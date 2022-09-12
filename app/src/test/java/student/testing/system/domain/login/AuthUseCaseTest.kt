package student.testing.system.domain.login

import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import student.testing.system.R
import student.testing.system.FakeRepository


@ExperimentalCoroutinesApi
class AuthUseCaseTest {

    private val repository = FakeRepository()
    private val prefsUtils = mockk<PrefsUtils>(relaxed = true)
    private val authUseCase = spyk(AuthUseCase(repository, prefsUtils))

    @Test
    fun `empty email returns EmailError`() = runTest {
        val expected = LoginState.EmailError(R.string.error_empty_field)
        val actual = authUseCase.invoke("", "").first()
        assertEquals(expected, actual)
    }

    @Test
    fun `invalid E-mail format returns EmailError`() = runTest {
        val expected = LoginState.EmailError(R.string.error_invalid_email)
        val actual = authUseCase.invoke("someEmail", "").first()
        assertEquals(expected, actual)
    }

    @Test
    fun `empty password returns PasswordError`() = runTest {
        val expected = LoginState.PasswordError(R.string.error_empty_field)
        val actual = authUseCase.invoke("test@mail.ru", "").first()
        assertEquals(expected, actual)
    }

    @Test
    fun `success auth returns PrivateUser`() = runBlocking {
        val expected = LoginState.Success(PrivateUser(1, "Ivan", "test@mail.ru", "some_token"))
        val actual = authUseCase.invoke("test@mail.ru", "pass").first()
        assertEquals(expected, actual)
    }

    @Test
    fun `failed auth returns Error`() = runBlocking {
        val expected = LoginState.Error("Incorrect username or password")
        val actual = authUseCase.invoke("other@mail.ru", "pass").first()
        assertEquals(expected, actual)
    }
}