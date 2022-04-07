package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccessGroup(
    val id: String,
    val name: String,
    val permissions: List<Permission>
):Parcelable