package student.testing.system.presentation.ui.models

import student.testing.system.domain.states.LoadableData
import student.testing.system.models.Test

data class TestsContentState(
    val tests: LoadableData<List<Test>> = LoadableData.NoState,
)