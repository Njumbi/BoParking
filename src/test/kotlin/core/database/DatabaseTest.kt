package core.database

import com.google.gson.JsonObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DatabaseTest {
    private lateinit var database: Database

    @BeforeEach
    fun setUp() {
        database = Database.Builder()
            .setDbName("test")
            .buildDb()
    }

    @AfterEach
    fun tearDown() {
        database.databaseFile.delete()
    }

    @Test
    fun `should pass given that the database file was created`() {
        assertNotNull(database.databaseFile)
    }

    private val mockData = JsonObject().apply {
        addProperty("name", "Florence")
        addProperty("age", "90")
    }

    @Test
    fun`should pass given that single data is inserted successfully `(){
        // Inserting the mockData into the database.
        database.insertData("user",mockData)

        // Reading the data from the database using the key "user".
        val data = database.readDataUsingKey<JsonObject>("user")
        // Checking if the data is not null.
        assertNotNull(data)
        // Checking if the data is equal to the mockData.
        assertEquals(data?.get("name")?.asString,"Florence")
    }

    @Test
    fun `should return null given that no data is available with that key `(){
        database.insertData("user",mockData)
        val data = database.readDataUsingKey<JsonObject>("user_1")
        assertNull(data)
    }

    @Test
    fun`should return list of objects given its available`(){
        //create a list of data
        val mockDataList = listOf<JsonObject>(mockData)

        //insert data
        database.insertData("user_1",mockDataList)

        val data = database.getListOfUsingKey<JsonObject>("user_1")
        assertNotNull( data )
        assertEquals(data.size, 1 )
        assertContains(data, mockData )
    }
    @Test
    fun`should return null if data is not available`(){
        //create a list of data
        val mockDataList = listOf<JsonObject>(mockData)

        //insert data
        database.insertData("user_1",mockDataList)

        val data = database.getListOfUsingKey<JsonObject>("user_2")
        assertNull( data )
    }
}