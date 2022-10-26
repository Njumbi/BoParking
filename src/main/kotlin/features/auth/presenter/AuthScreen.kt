package features.auth.presenter

import core.database.Database
import features.auth.data.AuthRepository
import features.auth.data.UserModel

object AuthScreen {
    fun adminUser(database: Database) {
        val authRepository = AuthRepository(database)
        println("\n")
        println("Admin portal. Select an option to continue:\n1.Login\n2.Register")
        when (readln()) {
            "1" -> {
                println("Enter your business name:")

                val businessName = readln()
                println("Enter your password:")

                val password = readln()
                println("Validating your request")

                // call out repository
                val isValidUser = authRepository.login(businessName, password)
                if (isValidUser == null) {
                    println("User not found. Kindly retry again")
                } else {
                    adminSlots(isValidUser)
                }
            }
            "2" -> {
                println("\n")
                println("Enter your business name:")
                val businessName = readln()
                if (businessName.isEmpty()) {
                    println("Invalid business name")
                }

                println("Enter your location:")
                val locationName = readln()
                if (businessName.isEmpty()) {
                    println("Invalid location name")
                }

                println("Enter your password:")
                val password = readln()
                if (businessName.isEmpty()) {
                    println("Invalid password")
                }

                val registration = authRepository.registerAdmin(
                    businessName,
                    locationName,
                    password
                )
                if (registration.first) {
                    adminSlots(registration.third!!)
                } else {
                    println("Error: ${registration.second}")
                }
            }
            else -> {
                println("Select a valid admin option")
            }
        }

    }

    private fun adminSlots(isValidUser: UserModel) {
        println("\n-----------------------------------------------------------")
        println("Welcome ${isValidUser.name}.\n Select an option to continue:\n1.Enter no. of parking slots\n2.Enter amount charges per hour")
        when (readln()) {
            "1" -> {

            }
            "2" -> {

            }
            else -> {
                println("Error: Select a valid option")
            }
        }
    }
}