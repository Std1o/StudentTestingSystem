package student.testing.system.presentation.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import student.testing.system.R
import student.testing.system.common.formatToString
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected: (String?) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    var selectedDate by remember { mutableLongStateOf(calendar.timeInMillis) }

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = { onDateSelected(null) },
        confirmButton = {
            TextButton(onClick = {
                selectedDate = datePickerState.selectedDateMillis!!
                val dateStr = Date(selectedDate).formatToString("yyyy-MM-dd")
                onDateSelected(dateStr)
            }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDateSelected(null) }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    ) { DatePicker(state = datePickerState) }
}