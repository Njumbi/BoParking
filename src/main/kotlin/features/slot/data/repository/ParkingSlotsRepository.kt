package features.slot.data.repository

import core.database.AppKeys
import core.database.Database
import features.auth.data.UserModel
import features.slot.data.models.ParkingSlotModel

class ParkingSlotsRepository(private val database: Database) {

    fun saveParkingSlots(userId: String, parkingSlotsNo: Int, amountChargedPerHour: Int): Pair<Boolean, String?> {
        return try {
            // check if user exists
            val users = database.getListOfUsingKey<UserModel>(AppKeys.userKey) ?: throw Exception("Users not found")
            val user = users.find { it.id == userId } ?: throw Exception("User not found")

            // save to db
            val parkingSlot = ParkingSlotModel(
                userId = userId,
                noOfParkingSlots = parkingSlotsNo,
                amountChargedPerHour = amountChargedPerHour
            )
            database.insertData(AppKeys.parkingSlot, listOf(parkingSlot))
            Pair(true, "Parking slot and amount charged per hour added")
        } catch (e: Exception) {
            Pair(false, e.message)
        }
    }
}