package student.testing.system.ui.fragments.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import student.testing.system.databinding.LoginFragmentBinding
import student.testing.system.ui.MainActivity


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var _binding: LoginFragmentBinding
    private val binding get() = _binding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_signUpFragment, bundle)
        }

        binding.btnLogin.setOnClickListener() {
            auth(binding.login.text.toString(), binding.password.text.toString())
        }

        return binding.root
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
                        val snackbar = Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        AccountSession.instance.token = it.data.access_token
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                }
            }
        }
    }
}