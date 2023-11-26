package student.testing.system.presentation.ui.models

import student.testing.system.domain.enums.OrderingType
import kotlin.properties.Delegates

class FiltersContainer(
    var showOnlyMaxResults: Boolean = false,
    var ratingRangeEnabled: Boolean = true,
    var lowerBound: Float = 0f,
    var upperBound: Float? = null,
    var scoreEqualsEnabled: Boolean = false,
    var scoreEqualsValue: Float? = null,
    var dateFrom: String? = null,
    var dateTo: String? = null,
    var orderingType: OrderingType? = null,
) {
    var maxScore: Int by Delegates.observable(0) { _, _, new ->
        upperBound = new.toFloat()
    }
}