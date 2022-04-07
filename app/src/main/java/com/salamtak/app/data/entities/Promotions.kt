package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Promotions(
    val id: String,
    val title: String,
    val subtitle: String,
    val backgroundColor: String,
    val imageUrl: String

) : Parcelable