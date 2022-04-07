package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attachment(
    var id: String,
    var position: Int,
    var thumbnailUrl: String,
    var type: Int,
    var canBeDeleted: Boolean = false
) : Parcelable