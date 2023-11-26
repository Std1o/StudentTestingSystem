package student.testing.system.presentation.ui.models.contentState

import stdio.godofappstates.annotations.ContentState
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.models.ParticipantResult
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test

@ContentState
data class ResultsContentState(
    val results: LoadableData<ParticipantsResults> = LoadableData.NoState,
)