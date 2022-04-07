package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SavedAddresse(
    val addressType: String,
    val area: Area?,
    val buildingNum: String,
    val city: City,
    val id: String,
    val lat: Double,
    val lng: Double,
    val streetAddress: String
):Parcelable