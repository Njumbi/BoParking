package features.slot.data.repository

import core.database.AppKeys
import core.database.Database
import features.auth.data.Role
import features.auth.data.UserModel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ParkingSlotsRepositoryTest {

    private lateinit var database: Database
    private lateinit var parkingSlotsRepository: ParkingSlotsRepository

    @BeforeEach
    fun setUp() {
        database = Database.Builder()
            .setDbName("test")
            .buildDb()

        // set up mock user
        val mockUser = UserModel(id = "200", name = "Test user", location = "Test location", role = Role.ADMIN)
        database.insertData(AppKeys.userKey, listOf(mockUser))

        parkingSlotsRepository = ParkingSlotsRepository(database)
    }

    @AfterEach
    fun tearDown() {
        database.databaseFile?.delete()
    }

    @Test
    fun `should save parking slot successfully`() {
        val saveParking = parkingSlotsRepository.saveParkingSlots("200", 90,120)
        assertEquals(saveParking.first, true)
    }

    @Test
    fun `should return false if user does not exist`() {
        val saveParking = parkingSlotsRepository.saveParkingSlots("100", 90,120)
        assertEquals(saveParking.first, false)
    }

}