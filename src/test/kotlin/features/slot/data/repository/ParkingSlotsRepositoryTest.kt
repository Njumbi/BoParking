package features.slot.data.repository

import core.database.AppKeys
import core.database.Database
import features.auth.data.Role
import features.auth.data.UserModel
import features.slot.data.models.ParkingSlotModel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

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
        val mockUser2 = UserModel(id = "300", name = "Test user Two", location = "Test location", role = Role.ADMIN)
        database.insertData(AppKeys.userKey, listOf(mockUser, mockUser2))

        parkingSlotsRepository = ParkingSlotsRepository(database)
    }

    @AfterEach
    fun tearDown() {
      //  database.databaseFile?.delete()
    }

    @Test
    fun `should save parking slot successfully`() {
        val saveParking = parkingSlotsRepository.saveParkingSlots("200", 90,120.00)
        val allParkingSlots = database.getListOfUsingKey<ParkingSlotModel>(AppKeys.parkingSlot)?.map { it.noOfParkingSlots }
        assertContains( allParkingSlots!!, 90 )
        assertEquals(saveParking.first, true)
    }

    @Test
    fun `should save parking slots without deleting previous one`() {
         parkingSlotsRepository.saveParkingSlots("200", 90,120.00)
         parkingSlotsRepository.saveParkingSlots("300", 300,80.00)

        val listOfParkingSlots = database.getListOfUsingKey<ParkingSlotModel>(AppKeys.parkingSlot)

        assertEquals( listOfParkingSlots?.size, 2 )
    }

    @Test
    fun `should return false if user does not exist`() {
        val saveParking = parkingSlotsRepository.saveParkingSlots("100", 90,120.00)
        assertEquals(saveParking.first, false)
    }

}