package com.salamtak.app.data.entities.cart

data class CarCategoryRequestBody(
    var type: Int? = 0,
    var installmentTypeId: Int? = 0,
    var downPayment: Int? = 0,
    var totalCost: Int? = 0,
    var providerId: String? = "",
    var cartUID: String? = "",
    var itemUID: String? = "",
    var deviceId: String? = null,
    var carService: CarService? = CarService(),
    var monthlyInstallment: Int? = 0
)
