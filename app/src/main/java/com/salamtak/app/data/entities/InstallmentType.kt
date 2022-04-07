package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InstallmentType(
    val id: String,
//    val financialProvider: FinancialProvider?,
    val name: String,
    val numberOfMonths: Int,
    val vat: Double,
    var monthlyAmount: Double = 0.0

) : Parcelable