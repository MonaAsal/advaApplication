package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedicalProviderDetails(
    val about: String?,
//    val category: Category
    val reviewsCount:Int,
    val maxDiscount: Double,
    val branches: List<Branch>?,
    val specialization: List<Specialization>?,
    var doctors: List<Doctor>?,
    val operations: List<Operation>?
) : MedicalProvider(), Parcelable
