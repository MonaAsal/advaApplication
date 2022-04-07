package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class MedicalNetwork(
    var id: String,
    var name: String
) : Parcelable {
    //    var id: String = ""
    val imageUrl: String? = ""

    //    var name: String = ""
    val logo: String? = ""
    val rate: Int = 0
    val type: Int? = 1
    val specialization: Specialization? = null

//    fun DoctorBase(
//
//    ) {
//        this.id = id
//        this.name = name
//    }
}
