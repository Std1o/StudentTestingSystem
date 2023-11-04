package student.testing.system.presentation.ui.screens.resultsReview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
            var onlyMaxResults by rememberSaveable { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .toggleable(
                        value = onlyMaxResults,
                        role = Role.Checkbox,
                        onValueChange = {
                            onlyMaxResults = !onlyMaxResults
                        }
                    )
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = onlyMaxResults,
                    onCheckedChange = {
                        onlyMaxResults = !onlyMaxResults
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
            var sliderPosition by remember { mutableStateOf(0f..100f) }
            RangeSlider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    // launch some business logic update with the state you hold
                    // viewModel.updateSelectedSliderValue(sliderPosition)
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            var scoreEquals by rememberSaveable { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .toggleable(
                        value = scoreEquals,
                        role = Role.Checkbox,
                        onValueChange = {
                            scoreEquals = !scoreEquals
                        }
                    )
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = scoreEquals,
                    onCheckedChange = {
                        scoreEquals = !scoreEquals
                    })
                Text(
                    text = stringResource(id = R.string.score_equals),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            var scoreValue by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = scoreValue,
                onValueChange = {
                    scoreValue = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(stringResource(id = R.string.score_value)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}