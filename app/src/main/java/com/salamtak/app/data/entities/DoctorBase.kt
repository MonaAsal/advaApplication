package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class DoctorBase(
    var id: String,
    var name: String
) : Parcelable {

    val imageUrl: String? = ""

    //    var name: String = ""
    val rate: Int = 0
    val specialization: Specialization? = null

//    fun DoctorBase(
//
//    ) {
//        this.id = id
//        this.name = name
//    }
}
