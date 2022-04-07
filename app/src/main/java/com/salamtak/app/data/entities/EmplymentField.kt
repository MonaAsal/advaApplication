package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmplymentField(
    val id: Int,
    val name: String
):Parcelable