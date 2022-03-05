package student.testing.system.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val constraintLayout = inflater.inflate(R.layout.login_fragment, container, false)
                as ConstraintLayout
        val name : TextView = constraintLayout.findViewById(R.id.name_field)
        val age : TextView = constraintLayout.findViewById(R.id.age_field)
        viewModel.getUser(0).observe(viewLifecycleOwner, {user ->
            name.text = user.access_token
        })
        return constraintLayout
    }

}