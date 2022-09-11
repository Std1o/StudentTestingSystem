package student.testing.system.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.*
import student.testing.system.databinding.FragmentSignUpBinding
import student.testing.system.presentation.ui.activity.MainActivity
import student.testing.system.presentation.viewmodels.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSignUp.setOnClickListener {
                if (!(nameLayout.isNotEmpty() && loginLayout.isValidEmail() && passwordLayout.isNotEmpty())) {
                    return@setOnClickListener
                }
                signUp(
                    email.text.trimString(),
                    name.text.trimString(),
                    password.text.trimString()
                )
            }
        }
    }

    private fun signUp(email: String, username: String, password: String) {
        viewModel.signUp(email, username, password).subscribeInUI(this, binding.progressBar) {
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}