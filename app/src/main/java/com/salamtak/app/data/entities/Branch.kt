package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Branch(
//    val area: Any,
//    var medicalProvider: MedicalProviderDetails?,
//    val buildingNum: String?,
//    val category: Category?,
//    val city: City,
    val contacts: List<Contact>?,
    val id: String,
//    val imageUrl: String?,
    val lat: Double?,
    val lng: Double?,
    val name: String,
    val rate: Int,
    val streetAddress: String,
//    val area: Area

) : Parcelable