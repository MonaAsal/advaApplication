package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeItem(
    val id: Int,
    val drawable: Int,
    val name: String
) : Parcelable