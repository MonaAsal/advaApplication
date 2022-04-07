package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class CarCategoryModel : Parcelable {
    val id:Int? = 0
    val name: String? = ""
    var carServiceProviders : ArrayList<CarProvidersModel> ?= arrayListOf()
}