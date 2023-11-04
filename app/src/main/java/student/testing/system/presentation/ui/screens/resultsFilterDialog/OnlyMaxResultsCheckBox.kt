package student.testing.system.presentation.ui.screens.resultsFilterDialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R

@Composable
fun OnlyMaxResultsCheckBox(value: Boolean, onValueChange: (Boolean) -> Unit) {
    var onlyMaxResults by rememberSaveable { mutableStateOf(value) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .toggleable(
                value = onlyMaxResults,
                role = Role.Checkbox,
                onValueChange = {
                    onlyMaxResults = it
                    onValueChange(it)
                }
            )
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = onlyMaxResults,
            onCheckedChange = {
                onlyMaxResults = it
                onValueChange(it)
            })
        Text(
            text = stringResource(id = R.string.only_max_results),
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}