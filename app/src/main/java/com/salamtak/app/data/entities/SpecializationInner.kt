package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecializationInner(val id: String, val name: String) : Parcelable
