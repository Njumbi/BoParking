package core.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import java.util.*
import kotlin.system.exitProcess

class Database : JsonParser by JsonParserImpl() {

    private var dataBaseName = "database"

    // Making the setter private to prevent the file from being mutated
    lateinit var databaseFile: File
        private set

    // It's a builder class that builds the database
    internal class Builder() {
        // Creating an instance of the Database class.
        private val database = Database()

        /**
         * It sets the database name.
         *
         * @param name The name of the database.
         * @return Builder
         */
        fun setDbName(name: String): Builder {
            database.dataBaseName = name
            return this
        }


        fun buildDb(): Database {
            database.syncDatabase()
            return database
        }
    }


    // check if file exists
    //  1. if not, create it
    // 2. if available continue
    private fun syncDatabase() {
        try {
            databaseFile = File("$dataBaseName.json")
            // if file exists no need of doing anything
            if (databaseFile.exists()) {
                return
            }
            // create the file and add first value - database version
            val createFile = databaseFile.createNewFile()
            if (createFile) {
                // create version
                val databaseVersion = DataBaseVersion(version = "1.0.0", dateModified = "${Date().time}")
                // write to file
                writeToFile("database", parseToString(databaseVersion))
            } else throw Exception("Unable to create database file")
        } catch (e: Exception) {
            println("Unable to sync database: ${e.toString()}")
            exitProcess(0)
        }
    }


    // convert data of type T to json string
    //  then store in terms of string
    inline fun <reified T> insertData(key: String, data: T) {
        try {
            if (databaseFile == null) {
                throw Exception("core.database.Database file is not yet set up")
            }
            writeToFile(key, parseToString(data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Reading all the data from the file and then parsing it to the object of the type T.
    inline fun <reified T> readDataUsingKey(key: String): T? {
        return try {
            val allData = readAllData()
            val data = allData[key]
            if (data != null) {
               parseToObject(data, T::class.java)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> getListOfUsingKey(key: String): List<T>? {
        return try {
            val allData = readAllData()
            val data = allData[key]
            if (data != null) {
                val type = TypeToken.getParameterized(List::class.java, T::class.java).type
                Gson().fromJson<List<T>>(data, type)
            } else
                null
        } catch (e: Exception) {
            null
        }
    }


    // Writing to a file.
    @Synchronized
    fun writeToFile(key: String, dataToWrite: String) {
        var fileWriter: OutputStreamWriter? = null
        var bufferWriter: BufferedWriter? = null
        try {

            // get previous data
            val previousData = readAllData()
            // add to previous data
            previousData[key] = dataToWrite

            fileWriter = databaseFile.writer()
            // buffer writer is recommended for large amounts of data
            bufferWriter = BufferedWriter(fileWriter)

            bufferWriter.write(parseToString(previousData))
            bufferWriter.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileWriter?.close()
                bufferWriter?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Reads all the data from the database file and returns it as a HashMap
    @Synchronized
    fun readAllData(): HashMap<String, String> {
        var bufferedReader: BufferedReader? = null
        return try {
            bufferedReader = databaseFile.bufferedReader()
            val data = bufferedReader.readText()
            if (data.isEmpty() || data.isBlank()) {
                HashMap<String, String>()
            } else {
                val type = object : TypeToken<HashMap<String, String>>() {}.type
                parseToObject(data, type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            HashMap<String, String>()
        } finally {
            bufferedReader?.close()
        }
    }
}