package com.carlosdev.randomusersapp.data

import com.carlosdev.randomusersapp.data.local.RealmDatabase
import com.carlosdev.randomusersapp.data.model.Coordinates
import com.carlosdev.randomusersapp.data.model.Dob
import com.carlosdev.randomusersapp.data.model.ID
import com.carlosdev.randomusersapp.data.model.Location
import com.carlosdev.randomusersapp.data.model.Login
import com.carlosdev.randomusersapp.data.model.Name
import com.carlosdev.randomusersapp.data.model.Picture
import com.carlosdev.randomusersapp.data.model.RandomUserResponse
import com.carlosdev.randomusersapp.data.model.Registered
import com.carlosdev.randomusersapp.data.model.Street
import com.carlosdev.randomusersapp.data.model.Timezone
import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.data.remote.RandomUserApi
import com.carlosdev.randomusersapp.data.repository.RandomUsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class RandomUsersRepositoryTest {

    private lateinit var api: RandomUserApi
    private lateinit var database: RealmDatabase
    private lateinit var repository: RandomUsersRepository
    private lateinit var fakeUser: User
    private lateinit var fakeUser2: User
    private lateinit var fakeUser3: User

    @Before
    fun setUp() {
        api = mockk()
        database = mockk()
        repository = RandomUsersRepository(api, database)

        fakeUser = User(
            gender = "male",
            id = ID(
                name = "fakeNameID",
                value = "123456"
            ),
            name = Name(
                title = "Mr",
                first = "John",
                last = "Doe"
            ),
            location = Location(
                street = Street(
                    number = 123,
                    name = "Any Street"
                ),
                city = "Anytown",
                state = "Anystate",
                country = "Anycountry",
                coordinates = Coordinates(
                    latitude = "123.456",
                    longitude = "456.789"
                ),
                timezone = Timezone(
                    offset = "+5:00",
                    description = "Any description"
                ),
            ),
            email = "john.doe@example.com",
            phone = "123-456-7890",
            cell = "098-765-4321",
            picture = Picture(
                large = "https://example.com/large.jpg",
                medium = "https://example.com/medium.jpg",
                thumbnail = "https://example.com/thumbnail.jpg"
            ),
            nat = "US",
            login = Login(
                uuid = "123456",
                username = "johndoe",
                password = "password",
                salt = "salt",
                md5 = "md5",
                sha1 = "sha1",
                sha256 = "sha256"
            ),
            dob = Dob(
                date = "1990-01-01T00:00:00Z",
                age = 32
            ),
            registered = Registered(
                date = "2021-01-01T00:00:00Z",
                age = 1
            )
        )

        fakeUser2 = User(
            gender = "male",
            id = ID(
                name = "fakeNameID2",
                value = "234567"
            ),
            name = Name(
                title = "Mr",
                first = "Jerry",
                last = "Cotton"
            ),
            location = Location(
                street = Street(
                    number = 123,
                    name = "Any Street"
                ),
                city = "Anytown",
                state = "Anystate",
                country = "Anycountry",
                coordinates = Coordinates(
                    latitude = "123.456",
                    longitude = "456.789"
                ),
                timezone = Timezone(
                    offset = "+1:00",
                    description = "Any description"
                ),
            ),
            email = "jerry.cotton@example.com",
            phone = "123-456-7890",
            cell = "098-765-4321",
            picture = Picture(
                large = "https://example.com/large.jpg",
                medium = "https://example.com/medium.jpg",
                thumbnail = "https://example.com/thumbnail.jpg"
            ),
            nat = "US",
            login = Login(
                uuid = "234567",
                username = "jerrycotton",
                password = "password",
                salt = "salt",
                md5 = "md5",
                sha1 = "sha1",
                sha256 = "sha256"
            ),
            dob = Dob(
                date = "1990-01-01T00:00:00Z",
                age = 32
            ),
            registered = Registered(
                date = "2021-01-01T00:00:00Z",
                age = 1
            )
        )

        fakeUser3 = User(
            gender = "female",
            id = ID(
                name = "fakeNameID3",
                value = "345678"
            ),
            name = Name(
                title = "Mrs",
                first = "Jessica",
                last = "Rabbit"
            ),
            location = Location(
                street = Street(
                    number = 123,
                    name = "Any Street"
                ),
                city = "Anytown",
                state = "Anystate",
                country = "Anycountry",
                coordinates = Coordinates(
                    latitude = "123.456",
                    longitude = "456.789"
                ),
                timezone = Timezone(
                    offset = "+2:00",
                    description = "Any description"
                ),
            ),
            email = "jessica.rabbit@example.com",
            phone = "123-456-7890",
            cell = "098-765-4321",
            picture = Picture(
                large = "https://example.com/large.jpg",
                medium = "https://example.com/medium.jpg",
                thumbnail = "https://example.com/thumbnail.jpg"
            ),
            nat = "US",
            login = Login(
                uuid = "345678",
                username = "jesicarabbit",
                password = "password",
                salt = "salt",
                md5 = "md5",
                sha1 = "sha1",
                sha256 = "sha256"
            ),
            dob = Dob(
                date = "1990-01-01T00:00:00Z",
                age = 32
            ),
            registered = Registered(
                date = "2021-01-01T00:00:00Z",
                age = 1
            )
        )

    }

    @Test
    fun `get users from api saves users to database`() = runTest {
        val users = listOf(fakeUser, fakeUser2, fakeUser3)
        coEvery { api.getUsers() } returns RandomUserResponse(results = users, info = mockk())
        coEvery { database.storeUsers(users) } returns Unit

        val result = repository.getUsersFromApi()

        assertEquals(users, result)
        coVerify { database.storeUsers(users) }
    }

    @Test
    fun `get users from database returns users`() = runTest {
        val users = listOf(fakeUser, fakeUser2, fakeUser3)
        coEvery { database.getUsers() } returns users

        val result = repository.getUsersFromDatabase()

        assertEquals(users, result)
    }

    @Test
    fun `get new users fetches from api and saves to database`() = runTest {
        val users = listOf(fakeUser, fakeUser2, fakeUser3)
        coEvery { api.getUsers() } returns RandomUserResponse(results = users, info = mockk())
        coEvery { database.storeUsers(users) } returns Unit
        coEvery { database.getUsers() } returns users

        val result = repository.getNewUsers()

        assertEquals(users, result)
        coVerify { database.storeUsers(users) }
    }

    @Test
    fun `save users stores users in database`() = runTest {
        val users = listOf(fakeUser, fakeUser2, fakeUser3)
        coEvery { database.storeUsers(users) } returns Unit

        repository.saveUsers(users)

        coVerify { database.storeUsers(users) }
    }

    @Test
    fun `delete user marks user as deleted in database`() = runTest {
        coEvery { database.markUserAsDeleted(fakeUser) } returns Unit

        repository.deleteUser(fakeUser)

        coVerify { database.markUserAsDeleted(fakeUser) }
    }

    @Test
    fun `get user by id returns correct user`() = runTest {
        coEvery { database.getUserById(fakeUser.id?.value!!) } returns fakeUser

        val result = repository.getUserById(fakeUser.id?.value!!)

        assertEquals(fakeUser, result)
    }

    @Test
    fun `get users from api with empty response`() = runTest {
        val users = emptyList<User>()
        coEvery { api.getUsers() } returns RandomUserResponse(results = users, info = mockk())
        coEvery { database.storeUsers(users) } returns Unit

        val result = repository.getUsersFromApi()

        assertEquals(users, result)
        coVerify { database.storeUsers(users) }
    }

    @Test
    fun `get users from database with empty database`() = runTest {
        val users = emptyList<User>()
        coEvery { database.getUsers() } returns users
        coEvery { api.getUsers() } returns RandomUserResponse(results = users, info = mockk())
        coEvery { database.storeUsers(users) } returns Unit

        val result = repository.getUsersFromDatabase()

        assertEquals(users, result)
    }

    @Test(expected = Exception::class)
    fun `get user by id with non-existent user`() = runTest {
        val nonExistentUserId = "non-existent-id"
        coEvery { database.getUserById(nonExistentUserId) } returns null

        repository.getUserById(nonExistentUserId)
    }

    @Test
    fun `delete user with non-existent user`() = runTest {
        val nonExistentUser = fakeUser
        coEvery { database.markUserAsDeleted(nonExistentUser) } returns Unit

        repository.deleteUser(nonExistentUser)

        coVerify { database.markUserAsDeleted(nonExistentUser) }
    }

    @Test
    fun `save users with empty list`() = runTest {
        val users = emptyList<User>()
        coEvery { database.storeUsers(users) } returns Unit

        repository.saveUsers(users)

        coVerify { database.storeUsers(users) }
    }
}