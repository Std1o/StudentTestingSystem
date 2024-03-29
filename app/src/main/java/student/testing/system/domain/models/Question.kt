package student.testing.system.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(val question: String, val answers: List<Answer>, val id: Int? = null): Parcelable
