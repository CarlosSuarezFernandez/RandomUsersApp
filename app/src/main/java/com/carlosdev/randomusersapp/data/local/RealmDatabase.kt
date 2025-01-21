package com.carlosdev.randomusersapp.data.local

import com.carlosdev.randomusersapp.data.local.model.RealmLocation
import com.carlosdev.randomusersapp.data.local.model.RealmLogin
import com.carlosdev.randomusersapp.data.local.model.RealmName
import com.carlosdev.randomusersapp.data.local.model.RealmPicture
import com.carlosdev.randomusersapp.data.local.model.RealmUser
import com.carlosdev.randomusersapp.data.model.ID
import com.carlosdev.randomusersapp.data.model.Location
import com.carlosdev.randomusersapp.data.model.Login
import com.carlosdev.randomusersapp.data.model.Name
import com.carlosdev.randomusersapp.data.model.Picture
import com.carlosdev.randomusersapp.data.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class RealmDatabase {

    val realm: Realm by lazy {
        val configuration = RealmConfiguration.Builder(
            schema = setOf(
                RealmUser::class,
                RealmName::class,
                RealmLocation::class,
                RealmLogin::class,
                RealmPicture::class
            )
        )
            .name("bronze_database.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.open(configuration)
    }

    fun closeRealm() {
        realm.close()
    }

    suspend fun storeUser(user: User) {
        realm.writeBlocking {
            val existingUser = query<RealmUser>(
                "id == $0",
                "${user.id.name}-${user.id.value}"
            ).first().find()
            if (existingUser != null) {
                throw IllegalArgumentException("User with this ID already exists")
            }

            val realmUser = RealmUser().apply {
                gender = user.gender
                name = RealmName().apply {
                    title = user.name.title
                    first = user.name.first
                    last = user.name.last
                }
                location = RealmLocation().apply {
                    street = user.location.street
                    city = user.location.city
                    state = user.location.state
                    postcode = user.location.postcode
                }
                email = user.email
                login = RealmLogin().apply {
                    username = user.login.username
                    password = user.login.password
                    salt = user.login.salt
                    md5 = user.login.md5
                    sha1 = user.login.sha1
                    sha256 = user.login.sha256
                }
                dob = user.dob
                registered = user.registered
                phone = user.phone
                cell = user.cell
                picture = RealmPicture().apply {
                    large = user.picture.large
                    medium = user.picture.medium
                    thumbnail = user.picture.thumbnail
                }
                nat = user.nat
                isDeleted = false
                id = "${user.id.name}-${user.id.value}"
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
        realm.writeBlocking {
            val existingUser = query<RealmUser>(
                "id == $0",
                "${user.id.name}-${user.id.value}"
            ).first().find()
            if (existingUser != null) {
                existingUser.isDeleted = true
            } else {
                throw IllegalArgumentException("User with this ID does not exist")
            }
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
                    street = realmUser.location?.street ?: "",
                    city = realmUser.location?.city ?: "",
                    state = realmUser.location?.state ?: "",
                    postcode = realmUser.location?.postcode ?: ""
                ),
                email = realmUser.email,
                login = Login(
                    username = realmUser.login?.username ?: "",
                    password = realmUser.login?.password ?: "",
                    salt = realmUser.login?.salt ?: "",
                    md5 = realmUser.login?.md5 ?: "",
                    sha1 = realmUser.login?.sha1 ?: "",
                    sha256 = realmUser.login?.sha256 ?: ""
                ),
                dob = realmUser.dob,
                registered = realmUser.registered,
                phone = realmUser.phone,
                cell = realmUser.cell,
                id = ID(
                    name = realmUser.id?.split("-")?.get(0) ?: "",
                    value = realmUser.id?.split("-")?.get(1) ?: ""
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


