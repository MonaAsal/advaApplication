package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    val id: String,
    val review: String?,
    val experienceRate: Int?,
    val medicalProviderRate: Int?,
    val doctorRate: Int?,
    val rate: Int?,
    val patient: BaseUser? = null,
    val createdAt: String,
    val isReviewed: Boolean?,
    val isBlocked: Boolean?,
    val blockReason: String?
) : Parcelable