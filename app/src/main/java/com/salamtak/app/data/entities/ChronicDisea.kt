package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChronicDisea(
    val chronicDiseas: ChronicDisease,
    val id: String,
    val infectionDate: String
//    val note: Any
):Parcelable