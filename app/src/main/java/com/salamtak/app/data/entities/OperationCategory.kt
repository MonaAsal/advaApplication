package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OperationCategory(
    val id: Int,
    val imageUrl: String?,
    val name: String
) : Parcelable