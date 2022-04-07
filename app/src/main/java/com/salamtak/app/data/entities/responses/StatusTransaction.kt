package com.salamtak.app.data.entities.responses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusTransaction(
    val createdAt: String,
    val id: String,
    val note: String,
    val status: String
) : Parcelable