package com.carlosdev.randomusersapp.data.local

import com.carlosdev.randomusersapp.data.local.model.RealmCoordinates
import com.carlosdev.randomusersapp.data.local.model.RealmDob
import com.carlosdev.randomusersapp.data.local.model.RealmLocation
import com.carlosdev.randomusersapp.data.local.model.RealmLogin
import com.carlosdev.randomusersapp.data.local.model.RealmName
import com.carlosdev.randomusersapp.data.local.model.RealmPicture
import com.carlosdev.randomusersapp.data.local.model.RealmRegistered
import com.carlosdev.randomusersapp.data.local.model.RealmStreet
import com.carlosdev.randomusersapp.data.local.model.RealmTimezone
import com.carlosdev.randomusersapp.data.local.model.RealmUser
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
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmScalarQuery

class RealmDatabase {

    val realm: Realm by lazy {
        val configuration = RealmConfiguration.Builder(
            schema = setOf(
                RealmUser::class,
                RealmCoordinates::class,
                RealmDob::class,
                RealmTimezone::class,
                RealmRegistered::class,
                RealmName::class,
                RealmLocation::class,
                RealmStreet::class,
                RealmLogin::class,
                RealmPicture::class
            )
        )
            .name("randomUsersDatabase.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.open(configuration)
    }

    fun closeRealm() {
        realm.close()
    }

    suspend fun storeUser(user: User) {
        if (user.id?.name.isNullOrEmpty() || user.id?.value.isNullOrEmpty()) {
            println("User ID is incomplete, not storing the user")
            return
        }

        realm.writeBlocking {
            val existingUserById = query<RealmUser>(
                "id == $0",
                "${user.id?.name}-${user.id?.value}"
            ).first().find()
            if (existingUserById != null) {
                // Handle the case where the user already exists by ID
                println("User with this ID already exists")
                return@writeBlocking
            }

            val existingUserByEmail = query<RealmUser>(
                "email == $0",
                user.email
            ).first().find()
            if (existingUserByEmail != null) {
                // Handle the case where the user already exists by email
                println("User with this email already exists")
                return@writeBlocking
            }

            val realmUser = RealmUser().apply {
                gender = user.gender
                name = RealmName().apply {
                    title = user.name.title
                    first = user.name.first
                    last = user.name.last
                }
                location = RealmLocation().apply {
                    street = RealmStreet().apply {
                        number = user.location.street.number
                        name = user.location.street.name
                    }
                    city = user.location.city
                    state = user.location.state
                    country = user.location.country
                    coordinates = RealmCoordinates().apply {
                        latitude = user.location.coordinates.latitude
                        longitude = user.location.coordinates.longitude
                    }
                    timezone = RealmTimezone().apply {
                        offset = user.location.timezone.offset
                        description = user.location.timezone.description
                    }
                }
                email = user.email
                login = RealmLogin().apply {
                    uuid = user.login.uuid
                    username = user.login.username
                    password = user.login.password
                    salt = user.login.salt
                    md5 = user.login.md5
                    sha1 = user.login.sha1
                    sha256 = user.login.sha256
                }
                dob = RealmDob().apply {
                    date = user.dob.date
                    age = user.dob.age
                }
                registered = RealmRegistered().apply {
                    date = user.registered.date
                    age = user.registered.age
                }
                phone = user.phone
                cell = user.cell
                picture = RealmPicture().apply {
                    large = user.picture.large
                    medium = user.picture.medium
                    thumbnail = user.picture.thumbnail
                }
                nat = user.nat
                isDeleted = false
                id = "${user.id?.name}+*+${user.id?.value}"
            }
            copyToRealm(realmUser)
        }
    }

    suspend fun storeUsers(users: List<User>) {
        users.forEach { user ->
            storeUser(user)
        }
    }

    suspend fun markUserAsDeleted(user: User) {
    try {
        realm.writeBlocking {
            val existingUser = query<RealmUser>(
                "id == $0",
                "${user.id?.name}+*+${user.id?.value}"
            ).first().find()
            if (existingUser != null) {
                existingUser.isDeleted = true
                println("User marked as deleted: ${user.id?.name}+*+${user.id?.value}")
            } else {
                println("User with this ID does not exist: ${user.id?.name}+*+${user.id?.value}")
            }
        }
    } catch (e: Exception) {
        println("Error marking user as deleted: ${e.message}")
    }
}

    suspend fun getUsers(): List<User> {
        return realm.query<RealmUser>("isDeleted == false").find().map { realmUser ->
            User(
                gender = realmUser.gender,
                name = Name(
                    title = realmUser.name?.title ?: "",
                    first = realmUser.name?.first ?: "",
                    last = realmUser.name?.last ?: ""
                ),
                location = Location(
                    street = Street(
                        number = realmUser.location?.street?.number ?: 0,
                        name = realmUser.location?.street?.name ?: ""
                    ),
                    city = realmUser.location?.city ?: "",
                    state = realmUser.location?.state ?: "",
                    country = realmUser.location?.country ?: "",
                    coordinates = Coordinates(
                        latitude = realmUser.location?.coordinates?.latitude ?: "",
                        longitude = realmUser.location?.coordinates?.longitude ?: ""
                    ),
                    timezone = Timezone(
                        offset = realmUser.location?.timezone?.offset ?: "",
                        description = realmUser.location?.timezone?.description ?: ""
                    )
                ),
                email = realmUser.email,
                login = Login(
                    uuid = realmUser.login?.uuid ?: "",
                    username = realmUser.login?.username ?: "",
                    password = realmUser.login?.password ?: "",
                    salt = realmUser.login?.salt ?: "",
                    md5 = realmUser.login?.md5 ?: "",
                    sha1 = realmUser.login?.sha1 ?: "",
                    sha256 = realmUser.login?.sha256 ?: ""
                ),
                dob = Dob(
                    date = realmUser.dob?.date ?: "",
                    age = realmUser.dob?.age ?: 0
                ),
                registered = Registered(
                    date = realmUser.registered?.date ?: "",
                    age = realmUser.registered?.age ?: 0
                ),
                phone = realmUser.phone,
                cell = realmUser.cell,
                id = ID(
                    name = realmUser.id.split("+*+")[0] ?: "",
                    value = realmUser.id.split("+*+")[1] ?: ""
                ),
                picture = Picture(
                    large = realmUser.picture?.large ?: "",
                    medium = realmUser.picture?.medium ?: "",
                    thumbnail = realmUser.picture?.thumbnail ?: ""
                ),
                nat = realmUser.nat
            )
        }
    }

    suspend fun getUserById(userId: String): User? {
    return realm.query<RealmUser>("id == $0", userId).first().find()?.let { realmUser ->
        val idParts = realmUser.id.split("+*+")
        User(
            gender = realmUser.gender,
            name = Name(
                title = realmUser.name?.title ?: "",
                first = realmUser.name?.first ?: "",
                last = realmUser.name?.last ?: ""
            ),
            location = Location(
                street = Street(
                    number = realmUser.location?.street?.number ?: 0,
                    name = realmUser.location?.street?.name ?: ""
                ),
                city = realmUser.location?.city ?: "",
                state = realmUser.location?.state ?: "",
                country = realmUser.location?.country ?: "",
                coordinates = Coordinates(
                    latitude = realmUser.location?.coordinates?.latitude ?: "",
                    longitude = realmUser.location?.coordinates?.longitude ?: ""
                ),
                timezone = Timezone(
                    offset = realmUser.location?.timezone?.offset ?: "",
                    description = realmUser.location?.timezone?.description ?: ""
                )
            ),
            email = realmUser.email,
            login = Login(
                uuid = realmUser.login?.uuid ?: "",
                username = realmUser.login?.username ?: "",
                password = realmUser.login?.password ?: "",
                salt = realmUser.login?.salt ?: "",
                md5 = realmUser.login?.md5 ?: "",
                sha1 = realmUser.login?.sha1 ?: "",
                sha256 = realmUser.login?.sha256 ?: ""
            ),
            dob = Dob(
                date = realmUser.dob?.date ?: "",
                age = realmUser.dob?.age ?: 0
            ),
            registered = Registered(
                date = realmUser.registered?.date ?: "",
                age = realmUser.registered?.age ?: 0
            ),
            phone = realmUser.phone,
            cell = realmUser.cell,
            id = ID(
                name = idParts.getOrNull(0) ?: "",
                value = idParts.getOrNull(1) ?: ""
            ),
            picture = Picture(
                large = realmUser.picture?.large ?: "",
                medium = realmUser.picture?.medium ?: "",
                thumbnail = realmUser.picture?.thumbnail ?: ""
            ),
            nat = realmUser.nat
        )
    }
}
}