package student.testing.system.presentation.ui.screens.participants

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import student.testing.system.R
import student.testing.system.presentation.ui.components.modifiers.noRippleClickable

@Composable
fun ParticipantContextMenu(
    modifier: Modifier,
    onAppointModerator: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_more_vert),
            contentDescription = "Показать меню",
            modifier = Modifier.noRippleClickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White),
        ) {
            Text(
                stringResource(R.string.appoint_moderator), modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        expanded = false
                        onAppointModerator()
                    })
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
            Text(
                stringResource(R.string.delete), modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        expanded = false
                        onDelete()
                    })
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }
}