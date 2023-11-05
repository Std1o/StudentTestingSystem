package student.testing.system.presentation.ui.components

import androidx.compose.runtime.Composable
import student.testing.system.domain.states.loadableData.LoadableData

@Composable
fun ListStateHandler(
    loadableData: LoadableData<List<*>>,
    onRetry: () -> Unit,
    onHideList: (Boolean) -> Unit
) {
    when (loadableData) {
        is LoadableData.Empty204 -> {
            onHideList(true)
        }

        is LoadableData.Error -> {
            onHideList(true)
            ErrorScreen(message = loadableData.exception, onRetry = onRetry)
        }

        is LoadableData.Loading -> {
            onHideList(false)
        }

        LoadableData.NoState -> {
            onHideList(false)
        }

        is LoadableData.Success -> {
            // TODO
            /*if (courses.data.isEmpty()) {
                ShowEmptyScreen()
            }*/
            onHideList(false)
        }
    }
}