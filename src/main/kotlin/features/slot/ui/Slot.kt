package features.slot.ui

import core.database.Database
import features.auth.data.UserModel
import features.slot.data.repository.ParkingSlotsRepository
import kotlin.system.exitProcess

object Slot {

    fun adminSlots(isValidUser: UserModel, database: Database) {
        println("\n-----------------------------------------------------------")
        println("Welcome ${isValidUser.name}.\nEnter no. of parking slots")
        while (true) {
            val parkingSlot = readln()
            if (parkingSlot.isEmpty() || !isNumeric(parkingSlot)) {
                println("parking slot is required")
            } else {
                println("Enter amount to charge per hour")
                val amount = readln()
                if (amount.isEmpty() || !isNumeric(amount)) {
                    println("Amount is required")
                } else {
                    // save
                    ParkingSlotsRepository(database).also {
                        val response = it.saveParkingSlots(
                            isValidUser.id,
                            parkingSlotsNo = parkingSlot.toInt(),
                            amountChargedPerHour = amount.toDouble()
                        )
                        if (response.first) {
                            // parking slot set
                            println("Parking slot set.")
                            exitProcess(0)
                        } else println("Unable to set parking slot: ${response.second}")
                    }

                }
            }
        }

    }

    fun isNumeric(strNum: String?): Boolean {
        if (strNum == null) {
            return false
        }
        try {
            val d = strNum.toDouble()
        } catch (nfe: NumberFormatException) {
            return false
        }
        return true
    }
}