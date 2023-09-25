package student.testing.system.presentation.viewmodels

/**
 * Indicates that the ViewModel state can be reset by composable function or subfunction
 */
interface ResettableViewModel {
    fun resetState()
}