package com.salamtak.app.data.entities.cart

data class HealthCategoryRequestBody(
    var type: Int? = null,
    var installmentTypeId: Int? = null,
    var downPayment: Int? = null,
    var totalCost: Int? = null,
    var providerId: String? = null,
    var cartUID: String? = null,
    var itemUID: String? = null,
    var deviceId: String? = null,
    var monthlyInstallment: Int? = null,
    var healthService: HealthService? = HealthService()
)

