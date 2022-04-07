package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OperationDetails(
    val hospitals: List<Hospital>,
    val operationId: String,
    val reviewsCount: Int,
    val details: String
) : Operation(), Parcelable