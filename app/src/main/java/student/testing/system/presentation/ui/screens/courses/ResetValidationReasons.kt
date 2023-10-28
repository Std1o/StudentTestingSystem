package student.testing.system.presentation.ui.screens.courses

sealed interface ResetValidationReasons {
    data object OnDialogDismissed : ResetValidationReasons

    data object OnTextFieldChanged : ResetValidationReasons

    data object OnValidationSuccess : ResetValidationReasons
}