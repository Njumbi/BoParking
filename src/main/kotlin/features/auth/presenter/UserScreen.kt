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
            println("-------------------------------------")
            println("No parking slots available")
        } else {
            listOfParkingIds = getParkingSlots.map { it.id }
            println("-------------------------------------")
            println(listOfParkingIds.toString())
        }
        println("-------------------------------------")
        println("Enter one of the parking Ids")
        var parkingId = readln()
        if (parkingId.isEmpty()) {
            println("-------------------------------------")
            println("Invalid parking Id")
        }
        val paymentRepository = PaymentRepository(database)
        println("-------------------------------------")
        println("Enter your car number plate")
        var carNoPlate = readln()
        if (carNoPlate.isEmpty()) {
            println("-------------------------------------")
            println("Invalid car No Plate")
        }
        //call repo method
        val parkResponse = paymentRepository.park(carNoPlate, parkingId)
        if (parkResponse.first) {
            println("------------------------------------- ")
            println("Welcome to our Parking,Go ahead and park your car in one of our parking spaces")
        } else {
            println("-------------------------------------\n${parkResponse.second}")
            println("-------------------------------------")
        }
    }

    fun displayAmountToPay(database: Database) {
        val paymentRepository = PaymentRepository(database)
        println("-------------------------------------")
        println("Enter your car number plate")
        var carNoPlate = readln()
        if (carNoPlate.isEmpty()) {
            println("-------------------------------------")
            println("Invalid car No Plate")
        }
        val paymentResponse = paymentRepository.displayPaymentToUser(carNoPlate)
        if (paymentResponse.first) {
            println("-------------------------------------")
            println("Your total amount is ${paymentResponse.third?.amount}\n")
            println("Continue to pay:\n1.Pay\n2.Cancel")
            println("-------------------------------------")
            when (readln()) {
                "1" -> {
                    // call payment
                    paymentRepository.pay(paymentResponse.third!!)
                    println("-------------------------------------")
                    println("Thank you for parking with us,see you next time")
                }
                "2" -> {
                    println("-------------------------------------")
                    println("Payment cancelled")
                }
                else -> {
                    println("-------------------------------------")
                    println("Invalid option")
                }
            }
        } else {
            println("-------------------------------------")
            println("Sorry, ${paymentResponse.second}")
        }
    }


}