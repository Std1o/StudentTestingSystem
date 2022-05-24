package student.testing.system.ui.fragments

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
import student.testing.system.databinding.FragmentLoginBinding
import student.testing.system.databinding.FragmentPassingTestBinding
import student.testing.system.databinding.FragmentTestsBinding
import student.testing.system.ui.activity.MainActivity
import student.testing.system.viewmodels.LoginViewModel


@AndroidEntryPoint
class TestPassingFragment : Fragment() {

    //private val viewModel by viewModels<LoginViewModel>()
    private lateinit var _binding: FragmentPassingTestBinding
    private val binding get() = _binding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassingTestBinding.inflate(inflater, container, false)
        return binding.root
    }
}