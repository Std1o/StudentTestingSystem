package student.testing.system.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParticipantsResults(val results: List<ParticipantResult>): Parcelable
