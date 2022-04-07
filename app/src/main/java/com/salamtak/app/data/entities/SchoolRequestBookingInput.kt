package com.salamtak.app.data.entities

data class SchoolRequestBookingInput(
    val DownPayment: String,
    val InstallmentTypeId: String,
    val Students: List<BaseStudent>?,
    val monthlyInstallment: String,
    val schoolId: String,
    val schoolBranchId: String,
    val isBusSubscriped: Boolean,
    val Bus_Fees: String,
    val TotalFees: String,
    val providerType: Int
)
