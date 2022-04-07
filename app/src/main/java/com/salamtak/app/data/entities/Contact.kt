package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val contact: String,
    val contactType: Int,
    val id: String,
    val createdAt: String
): Parcelable