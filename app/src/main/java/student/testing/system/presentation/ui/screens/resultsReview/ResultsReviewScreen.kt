package student.testing.system.presentation.ui.screens.resultsReview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.R
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.models.Test
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.SearchAppBar
import student.testing.system.presentation.ui.screens.resultsFilterDialog.ResultsFilterDialog
import student.testing.system.presentation.viewmodels.ResultsViewModel

@Composable
fun ResultsReviewScreen(test: Test) {
    val viewModel = hiltViewModel<ResultsViewModel>()
    val contentState by viewModel.contentState.collectAsState()
    var showBottomFiltersSheet by remember { mutableStateOf(false) }
    Surface {
        Scaffold(
            topBar = {
                SearchAppBar(actions = {
                    IconButton(onClick = { showBottomFiltersSheet = true }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                            contentDescription = "Localized description"
                        )
                    }
                }) {
                    viewModel.searchPrefix = it
                    viewModel.getResults()
                }
            }
        ) { contentPadding ->
            CenteredColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                var hideResultsList by remember { mutableStateOf(false) }
                ResultsList(
                    isLoading = contentState.results is LoadableData.Loading,
                    hidden = hideResultsList,
                    results = contentState.results,
                )
                UIReactionOnResultsListState(
                    loadableData = contentState.results,
                    onRetry = { viewModel.getResults() },
                    emptyListText = R.string.empty_results
                ) {
                    hideResultsList = it
                }
            }
        }
        if (showBottomFiltersSheet) {
            ResultsFilterDialog(viewModel.filtersContainer) {
                showBottomFiltersSheet = false
                viewModel.getResults()
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.setInitialData(test)
    }
}