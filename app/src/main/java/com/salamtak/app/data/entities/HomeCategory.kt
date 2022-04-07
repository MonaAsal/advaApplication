package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeCategory(
    val isFeatured: Boolean = false,
    val type: Int,
    val description: String,
    val imageUrl: String,
    val id: Int,
    var name: String

) : Parcelable