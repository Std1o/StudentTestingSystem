package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import stdio.godofappstates.annotations.PackageForStatesViewModel

// TODO не генерит если тут есть OperationState
// TODO попробовать сделать 2 процессора
@PackageForStatesViewModel
open class SpecialBaseViewModel<T> : ViewModel() {
}