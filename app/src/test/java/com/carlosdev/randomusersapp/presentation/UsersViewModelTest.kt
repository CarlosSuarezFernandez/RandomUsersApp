package com.carlosdev.randomusersapp.presentation

import com.carlosdev.randomusersapp.data.model.Coordinates
import com.carlosdev.randomusersapp.data.model.Dob
import com.carlosdev.randomusersapp.data.model.ID
import com.carlosdev.randomusersapp.data.model.Location
import com.carlosdev.randomusersapp.data.model.Login
import com.carlosdev.randomusersapp.data.model.Name
import com.carlosdev.randomusersapp.data.model.Picture
import com.carlosdev.randomusersapp.data.model.Registered
import com.carlosdev.randomusersapp.data.model.Street
import com.carlosdev.randomusersapp.data.model.Timezone
import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.domain.usecase.DeleteUserUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetNewRandomUsersUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetRandomUsersUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetUserByIDUsecase
import com.carlosdev.randomusersapp.utils.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private lateinit var viewModel: UsersViewModel
    private val getRandomUsersUsecase: GetRandomUsersUsecase = mockk()
    private val getNewRandomUsersUsecase: GetNewRandomUsersUsecase = mockk()
    private val deleteUserUsecase: DeleteUserUsecase = mockk()
    private val getUserByIDUsecase: GetUserByIDUsecase = mockk()
    private lateinit var fakeUser: User
    private lateinit var fakeUser2: User

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = UsersViewModel(
            getRandomUsersUsecase,
            getNewRandomUsersUsecase,
            deleteUserUsecase,
            getUserByIDUsecase
        )

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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUsers should fetch and update users`() = runTest {
        val users = listOf(fakeUser)
        coEvery { getRandomUsersUsecase.execute() } returns users

        viewModel.getUsers()
        viewModel.users.test {
            next()
            assertEquals(users, next())
        }
        coVerify { getRandomUsersUsecase.execute() }
    }

    @Test
    fun `getMoreUsers should fetch and append users`() = runTest {
        val initialUsers = listOf(fakeUser)
        val moreUsers = listOf(fakeUser, fakeUser2)
        val expectedUsers = listOf(fakeUser, fakeUser2)
        coEvery { getRandomUsersUsecase.execute() } returns initialUsers
        coEvery { getNewRandomUsersUsecase.execute() } returns moreUsers

        viewModel.getUsers()
        viewModel.getMoreUsers()

        viewModel.users.test {
            next()
            assertEquals(initialUsers, next())
            assertEquals(expectedUsers, next())
        }
        coVerify { getNewRandomUsersUsecase.execute() }
    }

    @Test
    fun `deleteUser should remove user from list`() = runTest {
        val users = listOf(fakeUser)
        coEvery { getRandomUsersUsecase.execute() } returns users
        coEvery { deleteUserUsecase.execute(fakeUser) } returns Unit

        viewModel.getUsers()
        viewModel.deleteUser(fakeUser)

        viewModel.users.test {
            next()
            assertEquals(users, next())
            assertEquals(emptyList<User>(), next())
        }
        coVerify { deleteUserUsecase.execute(fakeUser) }
    }

    @Test
    fun `filterUsers should filter users by search term`() = runTest {
        val users = listOf(fakeUser, fakeUser2)
        coEvery { getRandomUsersUsecase.execute() } returns users

        viewModel.getUsers()
        viewModel.filterUsers("Jane")

        viewModel.users.test {
            next()
            assertEquals(users, next())
            assertEquals(emptyList<User>(), next())
        }
    }

    @Test
    fun `getUserById should fetch user by ID`() = runTest {
        coEvery { getUserByIDUsecase.execute("1") } returns fakeUser

        viewModel.getUserById("1")

        viewModel.user.test {
            next()
            assertEquals(fakeUser, next())
        }
        coVerify { getUserByIDUsecase.execute("1") }
    }
}