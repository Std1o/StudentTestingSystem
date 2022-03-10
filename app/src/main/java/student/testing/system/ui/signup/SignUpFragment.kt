package student.testing.system.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.databinding.LoginFragmentBinding
import student.testing.system.databinding.SignUpFragmentBinding
import student.testing.system.ui.login.LoginViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel by viewModels<SignUpViewModel>()
    private lateinit var _binding: SignUpFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            MainScope().launch {
                viewModel.signUp(binding.email.text.toString(), binding.name.toString(), binding.password.text.toString()).collect { it
                    print(it.access_token)
                }
            }
        }


        return binding.root
    }

}