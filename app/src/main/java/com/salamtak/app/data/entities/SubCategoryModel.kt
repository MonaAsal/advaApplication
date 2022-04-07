package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
//class SubCategoryModel (
//    val id:Int = 0,
//    val name: String = "",
//    var operations : ArrayList<Operation> = arrayListOf()
//
//) : Parcelable

@Parcelize

open class SubCategoryModel : Parcelable{
    val id:Int? = 0
    val name: String? = ""
    var operations : ArrayList<Operation> ?= arrayListOf()

}