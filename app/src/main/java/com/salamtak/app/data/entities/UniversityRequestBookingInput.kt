package com.salamtak.app.data.entities

data class UniversityRequestBookingInput(
    val providerId: String,
    val CollegeId: String,
    val CollegeBranchId: String,
    val monthlyInstallment: String,
    val InstallmentTypeId: String,
    val DownPayment: String,
    val isBusSubscriped: Boolean,
    val Bus_Fees: String,
    val TotalFees: String,
    val Students: List<BaseStudent>?
)