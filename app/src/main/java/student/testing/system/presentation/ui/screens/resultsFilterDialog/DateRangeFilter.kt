package student.testing.system.presentation.ui.screens.resultsFilterDialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import student.testing.system.R
import student.testing.system.presentation.ui.components.ClickableTextField
import student.testing.system.presentation.ui.components.modifiers.noRippleClickable

@Composable
fun DateRangeFilter() {
    Row(
        modifier = Modifier
            .padding(top = 30.dp, bottom = 10.dp)
            .padding(horizontal = 10.dp),
    ) {
        ClickableTextField(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .noRippleClickable { println("aaaa") },
            label = { Text(stringResource(id = R.string.start)) },
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
                .padding(8.dp)
                .noRippleClickable { println("bbbb") },
            label = { Text(stringResource(id = R.string.end)) },
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
}