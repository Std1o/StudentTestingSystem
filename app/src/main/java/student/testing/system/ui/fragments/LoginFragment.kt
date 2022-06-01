package student.testing.system.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.*
import student.testing.system.databinding.FragmentLoginBinding
import student.testing.system.ui.activity.MainActivity
import student.testing.system.viewmodels.LoginViewModel


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_signUpFragment, bundle)
        }

        binding.btnLogin.setOnClickListener() {
            auth(binding.login.text.trimString(), binding.password.text.trimString())
        }
        if (!viewModel.isAuthDataSaved()) {
            binding.progressBar.showIf(false)
            binding.main.showIf(true)
        }
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.authStateFlow.subscribeInUI(this, binding.progressBar) {
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    private fun auth(email: String, password: String) {
        viewModel.auth(email, password)
    }
}