package com.salamtak.app.data.entities

data class FinishingRequestBookingInput(
    var providerId: String?,
    var PackageId: String?,
    var InstallmentTypeId: String,
    var DownPayment: String,
    var TotalCost: String,
    var FullName: String?,
    var PhoneNumber: String?,
    var ProviderName: String?,
    var MonthlyInstallment: String?
)