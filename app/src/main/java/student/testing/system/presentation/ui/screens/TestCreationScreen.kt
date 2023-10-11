package student.testing.system.presentation.ui.screens

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import student.testing.system.common.viewModelScopedTo
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@Composable
fun TestCreationScreen(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val sharedViewModel = navBackStackEntry
        ?.viewModelScopedTo<TestCreationViewModel>(
            navHostController,
            Destination.TestCreationScreen.fullRoute
        )
    Surface {
        CenteredColumn {
            Text(text = "Создание тестика ${sharedViewModel?.course}")
        }
    }
}