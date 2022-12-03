package student.testing.system.presentation.ui.fragments

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.subscribeInUI
import student.testing.system.common.viewBinding
import student.testing.system.databinding.FragmentResultsReviewBinding
import student.testing.system.presentation.ui.adapters.UsersResultsAdapter
import student.testing.system.presentation.ui.dialogFragments.ResultsFilterDialogFragment
import student.testing.system.presentation.viewmodels.ResultsSharedViewModel


@AndroidEntryPoint
class ResultsReviewFragment : Fragment(R.layout.fragment_results_review) {

    private val binding by viewBinding(FragmentResultsReviewBinding::bind)
    private val args: ResultsReviewFragmentArgs by navArgs()
    private lateinit var adapter: UsersResultsAdapter
    val viewModel: ResultsSharedViewModel by viewModels(
        ownerProducer = { this }
    )

    companion object {
        const val KEY_RESULTS_FILTER = "resultsFilter"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getResults(args.testId, args.courseId)
        binding.btnMenu.setOnClickListener(this::showPopup)
        subscribeObservers()
        initToolbar()
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {
            if (viewModel.maxScore == 0) viewModel.maxScore = it.maxScore
            adapter = UsersResultsAdapter(it.results, it.maxScore)
            binding.rv.layoutManager = LinearLayoutManager(requireContext())
            binding.rv.adapter = adapter
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            val searchView = menu.findItem(R.id.search).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchPrefix = newText
                    viewModel.getResults(args.testId, args.courseId)
                    return false
                }
            })
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> {
                    ResultsFilterDialogFragment
                        .newInstance(args.testId, args.courseId)
                        .show(childFragmentManager, KEY_RESULTS_FILTER);
                    true
                }
                else -> false
            }
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(requireActivity(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.results_context_menu, popup.menu)
        popup.show()
        popup.menu.getItem(0)
            .setTitle(if (viewModel.showOnlyMaxResults) R.string.all_results else R.string.max_results)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.results_filter -> {
                    viewModel.showOnlyMaxResults = !viewModel.showOnlyMaxResults
                    viewModel.getResults(args.testId, args.courseId)
                }
            }
            true
        }
    }
}