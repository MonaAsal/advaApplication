package com.salamtak.app.data.entities

data class CarCustomRequestBookingInput(
    var installmentTypeId: String?,
    var downPayment: String?,
    var totalCost: String?,
    var fullName: String?,
    var phoneNumber: String?,
    var providerName: String?,
    var monthlyInstallment: String?,
    var providerPhoneNumber: String?,
    var additionalInfo: String?,
    var carBrandId: Int?,
    var areaId: Int?,
    var cityId: Int?
    )