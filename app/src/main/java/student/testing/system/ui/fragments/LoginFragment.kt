package student.testing.system.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.showSnackbar
import student.testing.system.common.viewBinding
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
            auth(binding.login.text.toString(), binding.password.text.toString())
        }
    }

    private fun auth(email: String, password: String) {
        lifecycleScope.launch {
            viewModel.auth(email, password).collect {
                when (it) {
                    is DataState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is DataState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar(it.exception)
                    }
                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        AccountSession.instance.token = it.data.access_token
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                }
            }
        }
    }
}