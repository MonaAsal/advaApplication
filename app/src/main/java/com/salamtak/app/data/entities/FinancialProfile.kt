package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FinancialProfile(
    val id: String?,
    val idNumber: String?,
    val nationalIdExpireDate: String?,
    val firstName: String?,
    val middleName: String?,
    val lastName: String?,
    val familyName: String?,
    val building: String?,
    val streetAddress: String?,
    val cityId: String?,
    val areaId: String?,
    val mobile: String?,
    val maritalStatusId: String?,
    val idFaceUrl: String?,
    val idBackUrl: String?,
    val isDraft: Boolean,
    val financialProviderId: String?,
    var progress: Int,
    val customerFinancialGuarantor: FinancialProfile?,
    //val financialGuarantor: FinancialProfile?,
    val iDFaceTempName: String?,
    val iDBackTempName: String?
) : Parcelable