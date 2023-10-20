package student.testing.system.experimantal

import stdio.godofappstates.annotations.MyEvent

@MyEvent
data class TicketBookToClickedParams(
    val eventName: String,
    val screenName: String,
    val ticketNumber: Int,
    val ticketAmount: String,
)
