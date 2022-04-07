package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FinishingCategoryModel : Parcelable {
    val id:Int? = 0
    val name: String? = ""
    var finshingProviders : ArrayList<FinishingProvidersModel> ?= arrayListOf()
}