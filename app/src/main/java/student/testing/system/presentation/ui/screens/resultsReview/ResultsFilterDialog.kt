package student.testing.system.presentation.ui.screens.resultsReview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Divider
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.presentation.ui.components.CenteredColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsFilterDialog(onDismissRequest: () -> Unit) {
    val sheetState: SheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            Text(
                text = stringResource(R.string.filter),
                modifier = Modifier.padding(bottom = 5.dp),
                color = Color.Black,
                fontSize = 18.sp
            )
            Divider(color = Color.DarkGray, thickness = 0.5.dp)
            var checked by rememberSaveable { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .toggleable(
                        value = checked,
                        role = Role.Checkbox,
                        onValueChange = {
                            checked = !checked
                        }
                    )
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { isRight ->
                        checked = !checked
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
            Text(text = stringResource(id = R.string.ratings_range), fontSize = 14.sp)
        }
    }
}