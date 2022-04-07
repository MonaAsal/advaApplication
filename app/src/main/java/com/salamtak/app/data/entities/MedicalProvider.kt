package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class MedicalProvider : Parcelable {
    val id: String = ""
    val imageUrl: String? = ""
    val image: String? = ""
    val logo: String? = ""
    val name: String = ""
    val type: Int = 0
    val startFrom: Double? = 0.0
    val providerType: String? = ""
    val rate: Float? = 0f
    val maxMonth: Int? = 0

}