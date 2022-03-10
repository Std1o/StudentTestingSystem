package student.testing.system.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.DataState
import student.testing.system.databinding.LoginFragmentBinding


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
        _binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_signUpFragment, bundle)
        }

        lifecycleScope.launch {
            viewModel.auth()
                .collect {
                    when(it){
                        is DataState.Loading -> {
                            Log.d("status", "Loading")
                            print("Loading")
                        }
                        is DataState.Error -> {
                            Log.d("status", it.exception)
                            print(it.exception)
                        }
                        is DataState.Success -> {
                            Log.d("status", it.data.access_token)
                            print(it.data.access_token)

                        }
                    }
                }
        }

        return binding.root
    }

}