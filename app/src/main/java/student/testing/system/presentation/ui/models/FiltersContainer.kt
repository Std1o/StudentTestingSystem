package student.testing.system.presentation.ui.models

import student.testing.system.domain.enums.OrderingType
import kotlin.properties.Delegates

// TODO убрать var, сделать через ViewModel и copy
data class FiltersContainer(
    val showOnlyMaxResults: Boolean = false,
    val ratingRangeEnabled: Boolean = true,
    var lowerBound: Float = 0f,
    var upperBound: Float? = null,
    var scoreEqualsEnabled: Boolean = false,
    var scoreEqualsValue: Float? = null,
    var dateFrom: String? = null,
    var dateTo: String? = null,
    var orderingType: OrderingType? = null,
    val maxScore: Int = 0
) {
    fun copyWithMaxScore(maxScore: Int): FiltersContainer {
        return this.copy(
            maxScore = maxScore,
            upperBound = maxScore.toFloat()
        )
    }
}