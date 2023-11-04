package student.testing.system.presentation.ui.screens.resultsFilterDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RangeSlider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import student.testing.system.R
import student.testing.system.presentation.ui.components.MediumButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ResultsFilterDialog(onDismissRequest: () -> Unit, onSave: () -> Unit) {
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.filter),
                modifier = Modifier.padding(bottom = 5.dp),
                color = Color.Black,
                fontSize = 18.sp
            )
            Divider(color = Color.DarkGray, thickness = 0.5.dp)
            OnlyMaxResultsCheckBox {

            }
            Text(text = stringResource(id = R.string.ratings_range), fontSize = 14.sp)
            var sliderPosition by remember { mutableStateOf(0f..100f) }
            RangeSlider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    // do something
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            ScoreEqualsCheckBox {

            }
            var scoreValue by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = scoreValue,
                onValueChange = { scoreValue = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(stringResource(id = R.string.score_value)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )
            DateRangeFilter()
            OrderingTypeSelector()
            MediumButton(
                text = R.string.save,
                modifier = Modifier.padding(vertical = 30.dp)
            ) {
                onSave()
            }
        }
        LaunchedEffect(Unit) {
            println(sheetState.hasExpandedState)
        }
    }
}