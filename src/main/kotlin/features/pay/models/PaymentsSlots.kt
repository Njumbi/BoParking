package features.pay.models

data class PaymentModel(
    val id: String,
    val userId: Int? = null,
    val carPlate: String,
    var amount: Double? = null,
    val startingTime: Long?,
    var endingTime: Long? = null,
    val parkingSlot: String?
)