package features.slot.data.models

import java.util.UUID

data class ParkingSlotModel(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val noOfParkingSlots: Int?,
    val amountChargedPerHour: Double?,
)
