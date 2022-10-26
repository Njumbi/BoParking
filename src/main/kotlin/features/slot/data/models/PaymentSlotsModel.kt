package features.slot.data.models

data class PaymentSlotsModel(
    val id:Int,
    val userId: Int,
    val amount: Int?,
    val duration: Int?,
)
