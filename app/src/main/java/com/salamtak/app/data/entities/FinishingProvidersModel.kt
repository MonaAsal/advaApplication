package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FinishingProvidersModel : Parcelable {
    val id: String? = ""
    val name: String? = ""
    val isFavourite: Boolean?= false
    val rating: Float? = 0f
    val locations :ArrayList<String> ?= arrayListOf()
    val pricePerMeter : Double? = 0.0
    val categoryName: String? = ""
    val imageUrl: String? = ""
    val logoUrl: String? = ""
    val productType: Int = 0
    val pricingType: String = ""

}