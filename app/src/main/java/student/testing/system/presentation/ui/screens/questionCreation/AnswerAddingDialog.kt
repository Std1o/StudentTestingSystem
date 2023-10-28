package student.testing.system.presentation.ui.screens.questionCreation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import student.testing.system.R
import student.testing.system.domain.addQuestion.QuestionState
import student.testing.system.presentation.ui.components.InputDialog
import student.testing.system.presentation.ui.stateWrapper.QuestionStateWrapper

@Composable
fun AnswerAddingDialog(
    onDismiss: () -> Unit,
    onAnswerAdded: (String) -> Int,
) {
    var answerError by rememberSaveable { mutableIntStateOf(0) }
    InputDialog(
        titleResId = R.string.answer_adding,
        hintResId = R.string.input_answer,
        positiveButtonResId = R.string.create,
        isError = answerError != 0,
        errorText = answerError,
        onTextChanged = { answerError = 0 },
        onDismiss = {
            answerError = 0
            onDismiss()
        }
    ) {
        answerError = onAnswerAdded(it)
        if (answerError == 0) {
            onDismiss()
        }
    }
}