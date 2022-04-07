package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DoctorDetails(
    val bio: String?,
//    val category: Category
    val imageUrl: String?,
    val reviewsCount: Int,
    val maxDiscount: Double,
    val branches: List<Branch>?,
    val currentTitle: String,
    var contacts: List<Contact>?,
    val operations: List<Operation>?,
    val doctorMedicalProviders: List<WorkPlace>?,
    val rate: Float,
    val id: String = "",
//    val image: String = "",
    val name: String = "",
    val startFrom: Double? = 0.0
) : Parcelable
