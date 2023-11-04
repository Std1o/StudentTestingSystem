package student.testing.system.presentation.ui.screens.resultsFilterDialog

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import student.testing.system.R
import student.testing.system.models.enums.OrderingType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OrderingTypeSelector(value: OrderingType?, onItemSelected: (OrderingType?) -> Unit) {
    val items = OrderingType.getOrderingTypes()
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(value?.toString() ?: "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(),
            readOnly = true,
            value = selectedItem,
            onValueChange = {},
            label = { Text(stringResource(id = R.string.select_ordering_type)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.toString()) },
                    onClick = {
                        selectedItem = selectionOption.toString()
                        onItemSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}