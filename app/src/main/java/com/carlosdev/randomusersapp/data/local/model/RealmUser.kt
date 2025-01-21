package com.carlosdev.randomusersapp.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmUser : RealmObject {
    @PrimaryKey
    var id: String? = null
    var gender: String = ""
    var name: RealmName? = null
    var location: RealmLocation? = null
    var email: String = ""
    var login: RealmLogin? = null
    var dob: String = ""
    var registered: String = ""
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
    var street: String = ""
    var city: String = ""
    var state: String = ""
    var postcode: String = ""
}

class RealmLogin : RealmObject {
    var username: String = ""
    var password: String = ""
    var salt: String = ""
    var md5: String = ""
    var sha1: String = ""
    var sha256: String = ""
}

class RealmPicture : RealmObject {
    var large: String = ""
    var medium: String = ""
    var thumbnail: String = ""
}