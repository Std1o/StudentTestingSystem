package student.testing.system.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import student.testing.system.R
import student.testing.system.common.launchWhenStartedCollect
import student.testing.system.common.showIf
import student.testing.system.common.showSnackbar
import student.testing.system.common.trimString
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentSignUpBinding
import student.testing.system.domain.auth.AuthState
import student.testing.system.presentation.ui.activity.MainActivity
import student.testing.system.presentation.viewmodels.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginLayout.editText?.doOnTextChanged { _, _, _, _ ->
                loginLayout.error = null
            }
            passwordLayout.editText?.doOnTextChanged { _, _, _, _ ->
                passwordLayout.error = null
            }
            btnSignUp.setOnClickListener {
                viewModel.signUp(
                    email.text.trimString(),
                    name.text.trimString(),
                    password.text.trimString()
                )
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.onEach {
            binding.progressBar.showIf(it is AuthState.Loading)
            if (it is AuthState.Success) {
                requireActivity().finish()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            } else if (it is AuthState.Error) {
                showSnackbar(it.exception)
            } else if (it is AuthState.EmailError) {
                binding.loginLayout.error = getString(it.messageResId)
            } else if (it is AuthState.PasswordError) {
                binding.passwordLayout.error = getString(it.messageResId)
            }
        }.launchWhenStartedCollect(lifecycleScope)
    }
}