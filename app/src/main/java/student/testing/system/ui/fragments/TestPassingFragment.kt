package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.common.subscribeInUI
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentPassingTestBinding
import student.testing.system.models.Answer
import student.testing.system.models.UserAnswer
import student.testing.system.models.UserQuestion
import student.testing.system.ui.adapters.AnswersAdapter
import student.testing.system.viewmodels.TestPassingViewModel


@AndroidEntryPoint
class TestPassingFragment : Fragment(R.layout.fragment_passing_test) {

    private val viewModel: TestPassingViewModel by activityViewModels()
    private val binding by viewBinding(FragmentPassingTestBinding::bind)
    private val args: TestPassingFragmentArgs by navArgs()
    private lateinit var adapter: AnswersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val test = args.test
        var position = args.position
        val question = test.questions[position]
        binding.tvQuestion.text = question.question
        adapter = AnswersAdapter(test.questions[position].answers as ArrayList<Answer>, false, null)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        binding.btnNext.setOnClickListener {
            val userAnswers = arrayListOf<UserAnswer>()
            for (ans in adapter.dataList) {
                userAnswers += UserAnswer(ans.id!!, ans.isRight)
            }
            viewModel.userQuestions += UserQuestion(question.id!!, userAnswers)
            if (test.questions.count() - 1 == position) {
                viewModel.calculateResult(test.id, test.courseId)
                    .subscribeInUI(this, binding.progressBar) {
                        requireActivity().onBackPressed()
                    }
            } else {
                val action = TestPassingFragmentDirections.actionTestPassingFragmentSelf(test, ++position)
                findNavController().navigate(action)
            }

        }
    }
}