package student.testing.system.presentation.ui.models.contentState

import stdio.godofappstates.annotations.ContentState
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.Test

@ContentState
data class TestsContentState(
    val tests: LoadableData<List<Test>> = LoadableData.NoState,
)