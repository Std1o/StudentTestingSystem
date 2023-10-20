package student.testing.system.models.enums

enum class OrderingType {
    SCORE, SCORE_DESC, DATE, DATE_DESC;

    companion object {
        fun getOrderingTypes(): MutableList<OrderingType> {
            return mutableListOf(SCORE, SCORE_DESC, DATE, DATE_DESC)
        }
    }
}