package features.pay.models

import core.database.AppKeys
import core.database.Database
import features.auth.data.Role
import features.auth.data.UserModel
import features.slot.data.models.ParkingSlotModel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class PaymentRepositoryTest {

    private lateinit var database: Database
    private lateinit var paymentRepository: PaymentRepository

    @BeforeEach
    fun setUp() {
        database = Database.Builder().setDbName("test").buildDb()

        // set up mock user
        val mockUser = UserModel(id = "200", name = "Test user", location = "Test location", role = Role.ADMIN)
        database.insertData(AppKeys.userKey, listOf(mockUser))
        paymentRepository = PaymentRepository(database)
    }

    @AfterEach
    fun tearDown() {
        database?.databaseFile.delete()
    }

    @Test
    fun `should return false if no businesses found`() {
        val park = paymentRepository.park("KDE212N", "987")
        assertEquals(park.first, false)
        assertEquals(park.second, "Business not found")
    }

    @Test
    fun `should return false if parking no does not exist`() {
        database.insertData(
            AppKeys.parkingSlot, listOf(
                ParkingSlotModel(
                    userId = "200", noOfParkingSlots = 2, amountChargedPerHour = 100.00
                )
            )
        )

        val park = paymentRepository.park("KDE212N", "987")
        assertEquals(park.first, false)
        assertEquals(park.second, "Parking area by that id does not exist")
    }

    @Test
    fun `should return true if its the first parking here`() {
        val mockParkingSlotModel = ParkingSlotModel(
            userId = "200", noOfParkingSlots = 2, amountChargedPerHour = 100.00
        )
        database.insertData(
            AppKeys.parkingSlot, listOf(
                mockParkingSlotModel
            )
        )

        val park = paymentRepository.park("KDE212N", mockParkingSlotModel.id)

        assertEquals(park.first, true)
    }

    @Test
    fun `should return true if parking space is available`() {
        val mockParkingSlotModel = ParkingSlotModel(
            userId = "200", noOfParkingSlots = 2, amountChargedPerHour = 100.00
        )
        database.insertData(
            AppKeys.parkingSlot, listOf(
                mockParkingSlotModel
            )
        )
        // mock payment
        val mockPayment = PaymentModel(
            id = "100",
            carPlate = "KDE200N",
            startingTime = Date().time,
            parkingSlot = mockParkingSlotModel.id,
        )
        database.insertData(
            AppKeys.payments, listOf(
                mockPayment
            )
        )

        val park = paymentRepository.park("KDE212N", mockParkingSlotModel.id)

        assertEquals(park.first, true)
    }

    @Test
    fun `should return false if no parking space is available`() {
        val mockParkingSlotModel = ParkingSlotModel(
            userId = "200", noOfParkingSlots = 1, amountChargedPerHour = 100.00
        )
        database.insertData(
            AppKeys.parkingSlot, listOf(
                mockParkingSlotModel
            )
        )
        // mock payment
        val mockPayment = PaymentModel(
            id = "100",
            carPlate = "KDE200N",
            startingTime = Date().time,
            parkingSlot = mockParkingSlotModel.id,
        )
        database.insertData(
            AppKeys.payments, listOf(
                mockPayment
            )
        )

        val park = paymentRepository.park("KDE212N", mockParkingSlotModel.id)
        assertEquals(park.first, false)
        assertEquals(park.second, "No available parking space")
    }

    @Test
    fun `should return false if no car with such number plate  is found when user selects pay`() {
        val park = paymentRepository.prepPay("KDE212N")
        assertEquals(park.first, false)
        assertEquals(park.second, "No car parking record for KDE212N")
    }
    @Test
    fun `should return false if no car with the particular number plate is found`(){

    }
}