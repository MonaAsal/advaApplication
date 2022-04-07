package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddHomeVisitInput(
    var doctorSpecializationId: Int,
    var preferredTimeId: Int,
    var isForYou: Int, var name: String, var age: String,
    var notes: String, var contactNumber: String,
    var streetAddress: String, var buildingNum: String,
    var appartmentNumber: String, var cityId: Int,
    var areaId: String, var lat: Double,
    var lng: Double, var paymentMethodId: Int, var cardId: String
) : Parcelable