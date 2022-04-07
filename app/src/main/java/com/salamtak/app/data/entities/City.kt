package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val areas: List<Area>?,
    val id: Int,
    val name: String,
    val name2: String?="",
    var isSelected: Boolean = false

) : Parcelable