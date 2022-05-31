package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentResultReviewBinding
import student.testing.system.ui.adapters.UsersResultsAdapter


@AndroidEntryPoint
class ResultsReviewFragment : Fragment(R.layout.fragment_result_review) {

    private val binding by viewBinding(FragmentResultReviewBinding::bind)
    private val args: ResultsReviewFragmentArgs by navArgs()
    private lateinit var adapter: UsersResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val testResult = args.results
        adapter = UsersResultsAdapter(testResult.results, testResult.maxScore)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}