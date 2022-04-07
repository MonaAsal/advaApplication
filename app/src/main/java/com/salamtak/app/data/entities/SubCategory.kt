package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubCategory(
    val startsFrom: Double,
    val id: Int,
    val name: String,
    val drImagesUrls: List<String>
) : Parcelable