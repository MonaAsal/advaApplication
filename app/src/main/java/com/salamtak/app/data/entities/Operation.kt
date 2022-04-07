package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Operation : Parcelable {
    val id: String = ""
    val name: String? = ""
    val providerName: String? = ""
    val imageUrl: String = ""
    val rate: Float = 0f
    val startFrom: Double = 0.0
    val maxMonth: Int = 0
    var isFavourite: Boolean= false
    var owner: MedicalProvider? = null
    val operationName: String? = ""
    var categoryName : String?=""

    //    val id: String = ""
//    val rate: Float = 0f
//    var isFavourite: Boolean = false
//    val startFrom: Double = 0.0
//    val imageUrl: String = ""
//    val categoryName: String = ""
//    val details: String? = ""
}