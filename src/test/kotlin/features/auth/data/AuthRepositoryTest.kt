package features.auth.data

import core.database.AppKeys
import core.database.Database
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.xml.crypto.Data


class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var database: Database

    @BeforeAll
    fun setUp() {
        database = Database.Builder()
            .setDbName("test")
            .buildDb()
        authRepository = AuthRepository(database)
    }

    @AfterAll
    fun tearDown() {
        database.databaseFile.delete()
    }

    @Test
    fun `should return true given a user is successfully registered`() {
        // assert
        val registerUserResponse = authRepository.registerAdmin(
            "Bobos",
            location = "kahawa",
            password = "password"
        )

        assertEquals(registerUserResponse.first, true)
    }

    @Test
    fun `should return false given a user registration fails`() {
        // given
        val mockUsers = mutableListOf<UserModel>(
            UserModel(
                id = "1000",
                Role.ADMIN,
                "kahawa",
                password = "password",
                name = "Bobos"
            )
        )
        //when
        database.insertData(AppKeys.userKey, mockUsers)

        // assert
        val registerUserResponse = authRepository.registerAdmin(
            "Bobos",
            location = "kahawa",
            password = "password"
        )

        assertEquals(registerUserResponse.first, false)
    }
}