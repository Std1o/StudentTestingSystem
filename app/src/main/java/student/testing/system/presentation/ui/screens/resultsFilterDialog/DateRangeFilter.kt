package student.testing.system.presentation.ui.screens.resultsFilterDialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import student.testing.system.R
import student.testing.system.presentation.ui.components.ClickableTextField
import student.testing.system.presentation.ui.components.DatePickerDialog
import student.testing.system.presentation.ui.components.modifiers.noRippleClickable

@Composable
fun DateRangeFilter(dateFrom: String?, dateTo: String?, onDateRangeChanged: (String?, String?) -> Unit) {
    var showDateFromPicker by remember { mutableStateOf(false) }
    var showDateToPicker by remember { mutableStateOf(false) }
    var localDateFrom by remember { mutableStateOf(dateFrom) }
    var localDateTo by remember { mutableStateOf(dateTo) }

    Row(
        modifier = Modifier
            .padding(top = 30.dp, bottom = 10.dp)
            .padding(horizontal = 5.dp),
    ) {
        ClickableTextField(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .noRippleClickable { showDateFromPicker = true },
            label = { Text(localDateFrom ?: stringResource(id = R.string.start)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            leadingIcon = {
                Icon(
                    Icons.Filled.CalendarMonth, "calendar"
                )
            }
        )
        ClickableTextField(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .noRippleClickable { showDateToPicker = true },
            label = { Text(localDateTo ?: stringResource(id = R.string.end)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            leadingIcon = {
                Icon(
                    Icons.Filled.CalendarMonth, "calendar"
                )
            }
        )
    }
    if (showDateFromPicker) {
        DatePickerDialog {
            localDateFrom = it
            onDateRangeChanged(it, localDateTo)
            showDateFromPicker = false
        }
    }
    if (showDateToPicker) {
        DatePickerDialog {
            localDateTo = it
            onDateRangeChanged(localDateFrom, it)
            showDateToPicker = false
        }
    }
}