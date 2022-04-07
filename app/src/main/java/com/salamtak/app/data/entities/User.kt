package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var refreshToken: String,
    val status: String,
    var token: String,
    var firstName: String? = "",
    var secondName: String? = "",
    var thirdName: String? = "",
    var lastName: String? = "",
    var salamtakCardId: String? = "",
//    var basicProfile: BasicProfile?
) :BaseUser(), Parcelable

