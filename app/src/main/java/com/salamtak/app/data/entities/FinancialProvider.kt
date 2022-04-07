package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FinancialProvider(
    val id: String,
    val name: String
): Parcelable
