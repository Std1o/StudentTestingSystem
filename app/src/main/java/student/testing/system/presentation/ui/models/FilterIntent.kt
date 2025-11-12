package student.testing.system.presentation.ui.models

sealed interface FilterIntent {
    data class UpdateShowOnlyMaxResults(val showOnlyMaxResults: Boolean) : FilterIntent
    data class UpdateRatingRangeEnabled(val ratingRangeEnabled: Boolean) : FilterIntent
}