package features.slot.data.repository

import core.database.AppKeys
import core.database.Database
import features.auth.data.UserModel
import features.slot.data.models.ParkingSlotModel
import java.util.*

class ParkingSlotsRepository(private val database: Database) {

    fun saveParkingSlots(
        userId: String, parkingSlotsNo: Int, amountChargedPerHour: Double
    ): Pair<Boolean, String?> {
        return try {
            // check if user exists
            val checkUsers =
                database.getListOfUsingKey<UserModel>(AppKeys.userKey) ?: throw Exception("Users do not exist")
            checkUsers.find { it.id == userId } ?: throw Exception("User by that id does not exist")

            // soot
            val parkingSlotModel = ParkingSlotModel(
                UUID.randomUUID().toString(),
                userId = userId,
                noOfParkingSlots = parkingSlotsNo,
                amountChargedPerHour = amountChargedPerHour
            )

            // update parking slots
            var parkingSlots = database.getListOfUsingKey<ParkingSlotModel>(AppKeys.parkingSlot)
            if (parkingSlots == null) {
                parkingSlots = mutableListOf(parkingSlotModel)
            } else {
                // check if user has slots
                val userSlots = parkingSlots.find { it.userId == userId }
                if (userSlots != null) {
                    // update slots
                    userSlots.noOfParkingSlots = parkingSlotsNo
                    userSlots.amountChargedPerHour = amountChargedPerHour
                } else {
                    // add as new
                    parkingSlots
                        .add(parkingSlotModel)
                }
            }
            database.insertData(AppKeys.parkingSlot, parkingSlots)
            Pair(true, "")
        } catch (e: Exception) {
            Pair(false, e.message)
        }
    }

}