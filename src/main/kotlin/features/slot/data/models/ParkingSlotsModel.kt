package features.slot.data.models

import java.util.UUID

data class ParkingSlotModel(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    var noOfParkingSlots: Int?,
    var amountChargedPerHour: Double?,
)
