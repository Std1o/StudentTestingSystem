package student.testing.system.presentation.ui.screens.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import student.testing.system.R

@Composable
fun CoursesContextMenu(
    modifier: Modifier,
    onUserInfoDialogShow: () -> Unit,
    onLogoutDialogShow: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White),
        ) {
            Text(
                stringResource(R.string.who_am_i), modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        expanded = false
                        onUserInfoDialogShow()
                    })
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
            Text(
                stringResource(R.string.logout), modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        expanded = false
                        onLogoutDialogShow()
                    })
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }
}