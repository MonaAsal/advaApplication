package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeVisit(
    val appartmentNumber: String,
    val area: Area,
    val buildingNum: String,
    val city: City,
    val contactNumber: String,
    val createdAt: String,
    val doctor: Doctor,
//    val doctorPrescription: Any,
    val doctorSpecialization: DoctorSpecialization,
    val expectedVisitTime: String,
    val id: String,
    val isForYou: Boolean,
    val isPaid: Boolean,
    val lat: Double,
    val lng: Double,
    val notes: String,
    val patientName: String,
    val paymentMethod: PaymentMethod,
    val status: Int,
    val statusNote: String,
    val streetAddress: String,
    val toAfterDiscounttal: Double,
    val total: Double,
    val preferedTime: PreferedTime,
    val review: Review?

) : Parcelable