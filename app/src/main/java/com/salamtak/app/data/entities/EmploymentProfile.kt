package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmploymentProfile(
    val companyName: String,
    val employmentField: EmplymentField,
    val employmentType: EmploymentType,
    val hiringDate: String,
    val id: String,
    val netMonthlyIncome: String,
    val netMonthlyExpense: String,
    val profession: String,
    val workAddress: WorkAddress
):Parcelable