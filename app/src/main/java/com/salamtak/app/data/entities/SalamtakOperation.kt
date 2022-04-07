package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SalamtakOperation(
    val branch: Branch?,
    val id: String,
    var logo: String?,
    val installmentTypes: List<InstallmentType>,
    var imageUrl: String?,
    val price: Double,
    val rate: Int,
    var selected: Boolean = false,
    var title: String?,
    var doctor: DoctorBase?
) : Parcelable