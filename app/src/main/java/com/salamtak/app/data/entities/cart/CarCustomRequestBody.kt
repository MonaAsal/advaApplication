package com.salamtak.app.data.entities.cart

data class CarCustomRequestBody(
    var type: Int? = null,
    var installmentTypeId: Int? = null,
    var downPayment: Int? = null,
    var totalCost: Int? = null,
    var providerId: String? = null,
    var cartUID: String? = null,
    var itemUID: String? = null,
    var deviceId: String? = null,
    var custom: CarCustom? = CarCustom(),
    var carService: CarService? = CarService(),
    var MonthlyInstallment: Int? = null
)

data class CarCustom(
    var FullName: String? = null,
    var PhoneNumber: String? = null,
    var ProviderName: String? = null,
    var providerPhoneNumber: String? = null,
    var MonthlyInstallment: Int? = null,
    var AdditionalInfo: String? = null
)