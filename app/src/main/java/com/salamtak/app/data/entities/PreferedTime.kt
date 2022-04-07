package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreferedTime(
    val id: Int,
    val name: String
) : Parcelable