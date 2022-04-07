package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Providers(
    val id: String,
    val name: String,
    val imageUrl: String,
    val type: Int

) : Parcelable