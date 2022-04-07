package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Advertisement(
    val id: String,
    val imageUrl: String

) : Parcelable