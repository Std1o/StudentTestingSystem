package student.testing.system.presentation.ui.models

import student.testing.system.models.enums.OrderingType

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
)