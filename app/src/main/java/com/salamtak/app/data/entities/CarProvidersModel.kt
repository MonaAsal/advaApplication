package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class CarProvidersModel : Parcelable {

    val logoUrl: String? = ""
    val rating: Float? = 0f
    val isLiked: Boolean?= false
    //val services :ArrayList<String> ?= arrayListOf()
    val branches: Int = 0
    val isVerified: Boolean?= false
    val id: String? = ""
    val name: String? = ""

}