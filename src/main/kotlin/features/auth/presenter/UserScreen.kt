package features.auth.presenter

import core.database.AppKeys
import core.database.Database
import features.pay.models.PaymentRepository
import features.slot.data.models.ParkingSlotModel
import features.slot.data.repository.ParkingSlotsRepository

object UserScreen {
    fun parkUser(database: Database) {
        //display car area no
        val parkingSlotsRepository = ParkingSlotsRepository(database)
        var listOfParkingIds = emptyList<String>()
        val getParkingSlots = database.getListOfUsingKey<ParkingSlotModel>(AppKeys.parkingSlot)
        if (getParkingSlots == null) {
            println("No parking slots available")
        } else {
            listOfParkingIds = getParkingSlots.map { it.id }
            println(listOfParkingIds.toString())
        }
        println("Enter one of the parking Ids")
        var parkingId = readln()
        if (parkingId.isEmpty()) {
            println("Invalid parking Id")
        }
        val paymentRepository = PaymentRepository(database)
        println("Enter your car number plate")
        var carNoPlate = readln()
        if (carNoPlate.isEmpty()) {
            println("Invalid car No Plate")
        }
        //call repo method
        val parkResponse = paymentRepository.park(carNoPlate, parkingId)
        if (parkResponse.first)
            println("Welcome to our Parking, go ahead and park your car")
        else
            println("${parkResponse.second}")
        println("-------------------------------------")
    }

    fun displayAmountToPay(database: Database) {
        val paymentRepository = PaymentRepository(database)
        println("Enter your car number plate")
        var carNoPlate = readln()
        if (carNoPlate.isEmpty()) {
            println("Invalid car No Plate")
        }
        val paymentResponse = paymentRepository.displayPaymentToUser(carNoPlate)
        if (paymentResponse.first){
            println("Your total amount is ${paymentResponse.third?.amount}\n")
            println("Continue to pay:\n1.Pay\n2.Cancel")
            when (readln()) {
                "1" -> {
                    // call payment
                    paymentRepository.pay(paymentResponse.third!!)
                    println("Thank you for parking with us,see you next time")
                }
                "2" -> {
                    println("Payment cancelled")
                }
                else -> {
                    println("Invalid option")
                }
            }
        }else{
            println("Unable to park. ${paymentResponse.second}")
        }
    }


}