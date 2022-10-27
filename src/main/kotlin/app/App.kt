package app

import core.database.Database
import core.navigation.Navigator
import features.auth.presenter.AuthScreen
import features.auth.presenter.UserScreen

fun main() {

    // initiate db
    val database = Database.Builder()
        .setDbName("bobos_parking")
        .buildDb()

    while (true) {
        println("*************************************************************")
        println("Welcome ot Bo's parking system.\nHow can we help you:\n1.Park\n2.Pay")

        // check screen one options
        when (readln()) {
            Navigator.park -> {
                UserScreen.parkUser(database )
            }
            Navigator.pay -> {
                UserScreen.displayAmountToPay(database)
            }
            Navigator.admin -> {
                AuthScreen.adminUser(database)
            }
            else -> {
                println("-------------------------------------")
                println("Sorry! Please select a valid option")
            }
        }

    }
}



