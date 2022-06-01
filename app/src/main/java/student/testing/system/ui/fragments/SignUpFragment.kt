package student.testing.system.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.showSnackbar
import student.testing.system.common.subscribeInUI
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
        binding.btnSignUp.setOnClickListener {
            signUp(binding.email.text.toString(), binding.name.text.toString(), binding.password.text.toString())
        }
    }

    private fun signUp(email: String, username: String, password: String) {
        viewModel.signUp(email, username, password).subscribeInUI(this, binding.progressBar) {
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}