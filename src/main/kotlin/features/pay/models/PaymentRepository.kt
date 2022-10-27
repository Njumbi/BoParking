package features.pay.models

import core.database.AppKeys
import core.database.Database
import features.slot.data.models.ParkingSlotModel
import java.util.*

class PaymentRepository(val database: Database) {
    // 1. check if parking business exists
    // 2. check if parking slots is available
    // 3. check if there is any pending payment
    // 4. park car to a certain slot
    fun park(
        carNo: String,
        parkingAreaNo: String,
    ): Pair<Boolean, String?> {
        return try {
            // checking if parking business exists
            val businesses = database.getListOfUsingKey<ParkingSlotModel>(AppKeys.parkingSlot)
                ?: throw Exception("Business not found")
            val parkingSlot =
                businesses.find { it.id == parkingAreaNo } ?: throw Exception("Parking area by that id does not exist")

            val paymentModel = PaymentModel(
                id = UUID.randomUUID().toString(),
                carPlate = carNo,
                startingTime = Date().time,
                parkingSlot = parkingSlot.id
            )

            // check for parking slot
            var allPayments = database.getListOfUsingKey<PaymentModel>(AppKeys.payments)
            if (allPayments != null) {
                val occupiedSlots = allPayments.filter {
                    it.parkingSlot == parkingSlot.id && it.startingTime != null && it.endingTime == null && it.amount == null
                }.size
                val remainingSlots = parkingSlot.noOfParkingSlots?.minus(occupiedSlots) ?: 0
                if (remainingSlots > 0) {
                    // available parking slot - park
                    allPayments.add(paymentModel)
                } else throw Exception("Unable to park, since the are no parking spaces available")
            } else {
                allPayments = mutableListOf(paymentModel)
            }

            database.insertData(AppKeys.payments, allPayments)
            Pair(true, "")
        } catch (e: Exception) {
            Pair(false, e.message)
        }
    }

    //check existing record of number plate
    // if car number is present query details using the car number plate
    fun displayPaymentToUser(
        carNo: String
    ): Triple<Boolean, String?, PaymentModel?> {
        return try {
            val carPayments = database.getListOfUsingKey<PaymentModel>(AppKeys.payments)
                ?: throw Exception("No car parking record for $carNo")
            // Checking if the car is parked in the parking area.
            val carRequestingPayment = carPayments.find { it.carPlate == carNo && it.endingTime == null && it.amount == null }
                ?: throw Exception("Looks like your car has not been parked in this premises")


            // check if parking exists
            val parkingSlot = database.getListOfUsingKey<ParkingSlotModel>(AppKeys.parkingSlot)
           val currentParkingSlot = parkingSlot?.find { it.id == carRequestingPayment.parkingSlot }
                ?: throw Exception("Unable to find parking area number")

            // update payment
            carRequestingPayment.endingTime = Date().time
            carRequestingPayment.amount = (((carRequestingPayment.endingTime!! - carRequestingPayment.startingTime!!) / 1000)) * currentParkingSlot.amountChargedPerHour!!;

            Triple(true, "", carRequestingPayment)
        } catch (e: Exception) {
            Triple(false, e.message, null)
        }

    }

    fun pay(paymentModel: PaymentModel): Pair<Boolean, String?> {
        return try {
            var allPayments = database.getListOfUsingKey<PaymentModel>(AppKeys.payments)
            if (allPayments != null) {
                val carWantingToPay = allPayments.find { it.id == paymentModel.id } ?: throw Exception("Payment not found")
                carWantingToPay.endingTime = paymentModel.endingTime
                carWantingToPay.amount = paymentModel.amount

                database.insertData(AppKeys.payments, allPayments)
            }
            Pair(true, "Thank you for parking with us")
        } catch (e: Exception) {
            Pair(false, e.message)
        }
    }
}