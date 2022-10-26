package features.auth.data

import core.database.AppKeys
import core.database.Database
import java.util.*

/**
 *  This class handles all auth of the app
 *  1. business login
 *  2. business registration
 *  3. Other
 */
class AuthRepository(private val database: Database) {

    fun registerAdmin(
        name: String,
        location: String,
        password: String,
    ): Triple<Boolean, String?, UserModel?> = try {
        val userObject = UserModel(
            id = UUID.randomUUID().toString(), role = Role.ADMIN, location = location, password = password, name = name
        )

        // store data in terms of a list
        var previousBusinesses = database.getListOfUsingKey<UserModel>(AppKeys.userKey)
        if (previousBusinesses == null) {
            // no previous data found
            previousBusinesses = mutableListOf(userObject)
        } else {
            // get previous users and add this one
            val userExists = previousBusinesses.find { it.name == name }
            if (userExists != null)
                throw Exception("User by that name already exists")
            previousBusinesses.toMutableList().add(userObject)
        }
        database.insertData(AppKeys.userKey, previousBusinesses)
        Triple(true, "Success", userObject)
    } catch (e: Exception) {
        Triple(false, e.message, null)
    }

    fun login(name: String, password: String): UserModel? {
        val users = database.getListOfUsingKey<UserModel>(AppKeys.userKey) ?: return null
        return users.find { it.name == name && it.password == password }
    }

}