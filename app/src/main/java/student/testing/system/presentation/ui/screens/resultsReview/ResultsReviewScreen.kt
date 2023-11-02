package student.testing.system.presentation.ui.screens.resultsReview

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.Test
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.ResultsViewModel

@Composable
fun ResultsReviewScreen(test: Test) {
    val viewModel = hiltViewModel<ResultsViewModel>()
    val contentState by viewModel.contentState.collectAsState()
    Surface {
        CenteredColumn {
            Text(text = "Просмотр результатов")
            ResultsList(
                isLoading = contentState.results is LoadableData.Loading,
                hidden = false,
                results = contentState.results,
            )
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.setInitialData(test)
    }
}