package student.testing.system.ui.fragments.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import student.testing.system.databinding.SignUpFragmentBinding

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