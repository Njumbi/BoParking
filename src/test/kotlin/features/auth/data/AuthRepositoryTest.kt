package features.auth.data

import core.database.AppKeys
import core.database.Database
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var database: Database

    @BeforeEach
    fun setUp() {
        database = Database.Builder()
            .setDbName("test")
            .buildDb()
        authRepository = AuthRepository(database)
    }

    @AfterEach
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
    fun `should return true given more than users can be registered`() {
        // assert
      authRepository.registerAdmin(
            "Bobos",
            location = "kahawa",
            password = "password"
        )
      authRepository.registerAdmin(
            "Toni ",
            location = "kahawa",
            password = "toni"
        )

        val data  = database.getListOfUsingKey<UserModel>(AppKeys.userKey)
        assertEquals( data?.size, 2 )
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