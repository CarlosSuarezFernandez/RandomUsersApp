package com.carlosdev.randomusersapp.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmUser : RealmObject {
    @PrimaryKey
    var id: String = ""
    var gender: String = ""
    var name: RealmName? = null
    var location: RealmLocation? = null
    var email: String = ""
    var login: RealmLogin? = null
    var dob: RealmDob? = null
    var registered: RealmRegistered? = null
    var phone: String = ""
    var cell: String = ""
    var picture: RealmPicture? = null
    var nat: String = ""
    var isDeleted: Boolean = false
}

class RealmName : RealmObject {
    var title: String = ""
    var first: String = ""
    var last: String = ""
}

class RealmLocation : RealmObject {
    var street: RealmStreet? = null
    var city: String = ""
    var state: String = ""
    var country: String = ""
    var coordinates: RealmCoordinates? = null
    var timezone: RealmTimezone? = null
}

class RealmStreet : RealmObject {
    var number: Int = 0
    var name: String = ""
}

class RealmCoordinates : RealmObject {
    var latitude: String = ""
    var longitude: String = ""
}

class RealmTimezone : RealmObject {
    var offset: String = ""
    var description: String = ""
}

class RealmLogin : RealmObject {
    var uuid: String = ""
    var username: String = ""
    var password: String = ""
    var salt: String = ""
    var md5: String = ""
    var sha1: String = ""
    var sha256: String = ""
}

class RealmDob : RealmObject {
    var date: String = ""
    var age: Int = 0
}

class RealmRegistered : RealmObject {
    var date: String = ""
    var age: Int = 0
}

class RealmPicture : RealmObject {
    var large: String = ""
    var medium: String = ""
    var thumbnail: String = ""
}