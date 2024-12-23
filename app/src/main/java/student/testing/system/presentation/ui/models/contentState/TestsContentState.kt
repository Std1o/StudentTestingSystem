package student.testing.system.presentation.ui.models.contentState

import stdio.lilith.annotations.ContentState
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.models.Test

@ContentState
data class TestsContentState(
    val tests: LoadableData<List<Test>> = LoadableData.NoState,
)