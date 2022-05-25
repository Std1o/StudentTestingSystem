package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentPassingTestBinding
import student.testing.system.ui.adapters.AnswersAdapter


@AndroidEntryPoint
class TestPassingFragment : Fragment(R.layout.fragment_passing_test) {

    //private val viewModel by viewModels<LoginViewModel>()
    private val binding by viewBinding(FragmentPassingTestBinding::bind)
    private val args: TestPassingFragmentArgs by navArgs()
    private lateinit var adapter: AnswersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val test = args.test
        val question = test.questions[0]
        binding.tvQuestion.text = question.question
        adapter = AnswersAdapter(test.questions[0].answers)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}