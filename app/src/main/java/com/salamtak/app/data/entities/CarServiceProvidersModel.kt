package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CarServiceProvidersModel  (
    var logoUrl: String? = "",
    var rating: Int? = 0,
    var isLiked: Boolean? = false,
    var services: ArrayList<String> = arrayListOf(),
    var branches: Int? = 0,
    var isVerified: Boolean? = false,
    var id: String? = "",
    var name: String? = ""
)
