package features.pay.models

data class PaymentsSlots(
    val id: Int,
    val userId: Int,
    val amount: Int?,
    val startingTime: Int?,
    val endingTime: Int?,
    val parkingSlot: String?
)