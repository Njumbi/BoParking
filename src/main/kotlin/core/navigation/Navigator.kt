package core.navigation

sealed class Navigator {
    companion object {
        // special commands
        const val admin: String = "admin"

        // routes
        const val park: String = "1"
        const val pay: String = "2"
        const val adminLogin: String = "a.1"
        const val adminRegister: String = "a.2"

        // screen 1 options
        fun getScreenOneCommands(): List<String> = listOf(Navigator.admin, Navigator.park, Navigator.pay)
    }

    data class Admin(val name: String = "admin") : Navigator()
}