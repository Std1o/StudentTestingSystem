package student.testing.system.presentation.ui.screens.courses

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import student.testing.system.R
import student.testing.system.common.AccountSession

@Composable
fun AlertUserInfoDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.we_remind_you))
        },
        text = {
            val account = AccountSession.instance
            Text(
                text = stringResource(
                    R.string.user_info,
                    account.username ?: "",
                    account.email ?: ""
                )
            )
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(stringResource(id = R.string.thanks))
            }
        },
        containerColor = Color.White
    )
}