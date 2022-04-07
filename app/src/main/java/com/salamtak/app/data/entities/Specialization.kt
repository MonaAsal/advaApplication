package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Specialization(
    val id: String,
    val name: String?,
    val specialization: SpecializationInner?

): Parcelable