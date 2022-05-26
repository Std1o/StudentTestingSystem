package student.testing.system.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentPassingTestBinding
import student.testing.system.databinding.FragmentResultReviewBinding
import student.testing.system.models.Answer
import student.testing.system.models.UserAnswer
import student.testing.system.models.UserQuestion
import student.testing.system.ui.adapters.AnswersAdapter
import student.testing.system.ui.adapters.UserResultAdapter
import student.testing.system.ui.adapters.UsersResultsAdapter
import student.testing.system.viewmodels.TestCreationViewModel
import student.testing.system.viewmodels.TestPassingViewModel
import student.testing.system.viewmodels.TestsViewModel


@AndroidEntryPoint
class ResultsReviewFragment : Fragment(R.layout.fragment_result_review) {

    private val binding by viewBinding(FragmentResultReviewBinding::bind)
    private val args: ResultsReviewFragmentArgs by navArgs()
    private lateinit var adapter: UsersResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val testResult = args.results
        adapter = UsersResultsAdapter(testResult.results)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}