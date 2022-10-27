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
        // Creating a new user object with the given parameters
        val userObject = UserModel(
            id = UUID.randomUUID().toString(), role = Role.ADMIN, location = location, password = password, name = name
        )

        // store data in terms of a list
        // Getting the list of businesses from the database
        var previousBusinesses = database.getListOfUsingKey<UserModel>(AppKeys.userKey)
        if (previousBusinesses == null) {
            // if no previous data found, create a new list
            previousBusinesses = mutableListOf(userObject)
        } else {
            // get previous users and add this one
            // Checking if a user with the same name already exists.
            val userExists = previousBusinesses.find { it.name == name }
            // add user to list if not null
            if (userExists != null)
                throw Exception("User by that name already exists")
            previousBusinesses.add(userObject)
        }
        // Inserting the data into the database.
        database.insertData(AppKeys.userKey, previousBusinesses)
        // Returning a triple of values.
        Triple(true, "Success", userObject)
    } catch (e: Exception) {
        Triple(false, e.message, null)
    }

    fun login(name: String, password: String): UserModel? {
        val users = database.getListOfUsingKey<UserModel>(AppKeys.userKey) ?: return null
        return users.find { it.name == name && it.password == password }
    }

}