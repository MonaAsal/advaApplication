package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BasicProfile(
//    val gender: String,
//    val isEgyptian: Boolean,
//    val secondName: String,
//    val thirdName: String,
//    val nationalId: String,
//    val nationalIdExpireDate: String,
//    val nationalIdImageUrl: String,
//    val passportExpireDate: String,
//    val passportImageUrl: String,
//    val passportNumber: String,
//    val residentalStatus: ResidentalStatus?,
//    val salamtakCardId: String,
//    val savedAddresses: List<SavedAddresse>,
    var firstName: String = "",
    var lastName: String = "",
    var imageUrl: String = ""
) :  Parcelable