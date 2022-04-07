package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChronicDisease(
//    val generalInformation: Any,
    val id: Int,
    val name: String,
//    val syndrome: Any
    var hasThisChronic: Boolean
//    var selected: Boolean = false
) : Parcelable