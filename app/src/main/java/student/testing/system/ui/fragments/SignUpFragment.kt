package student.testing.system.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.subscribeInUI
import student.testing.system.common.trimString
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentSignUpBinding
import student.testing.system.ui.activity.MainActivity
import student.testing.system.viewmodels.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSignUp.setOnClickListener {
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