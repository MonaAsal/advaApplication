package com.salamtak.app.data.entities

data class CarServiceRequestBookingInput(
    val ProviderId: String?,
   // val CollegeId: String,
  //  val CollegeBranchId: String,
    val InstallmentTypeId: String,
    val DownPayment: String,
  //  val isBusSubscriped: Boolean,
   // val Bus_Fees: String,
    val TotalCost: String,
    val MonthlyInstallment: String?,

    // val Students: List<BaseStudent>?
)