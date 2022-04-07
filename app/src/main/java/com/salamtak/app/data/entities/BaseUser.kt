package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseUser : Parcelable {
    var email: String? = ""
    val id: String = ""
    var isMailVerified: Boolean = false
    var isPhoneVerified: Boolean = false
    var phone: String = ""
    val username: String = ""
    var imageUrl: String? = ""
    val name: String? = ""
}
