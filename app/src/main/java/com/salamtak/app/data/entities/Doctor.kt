package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doctor(
    val availableForHomeVisit: Boolean,
//    val experiences: List<Any>,
    val homeVisitFees: Double,
    val homeVisitVat: Double,
    val visitFees: Double,
    var id: String,
    val imageUrl: String?,
    var name: String,
    val rate: Int,
    val bio: String?,
    val specialization: Specialization?,
    val currentTitle: String?,
    val ownerMedicalProvider: MedicalProviderDetails?,
    val contacts: List<Contact>?,
    var workedAt: List<WorkPlace>?,
    var operations: List<Operation>?
) : Parcelable {
    //},DoctorBase(){
    constructor(
        id: String,
        name: String
    ) : this(
        false, 0.0, 0.0, 0.0, id, "", name, 0, "",
        null, "", null, null, null, null
    )
}