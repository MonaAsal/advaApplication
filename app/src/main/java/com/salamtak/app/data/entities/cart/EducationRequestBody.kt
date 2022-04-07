package com.salamtak.app.data.entities.cart

data class EducationRequestBody(
    var type: Int? = null,
    var installmentTypeId: Int? = null,
    var downPayment: Int? = null,
    var totalCost: Int? = null,
    var providerId: String? = null,
    var cartUID: String? = null,
    var itemUID: String? = null,
    var deviceId: String? = null,
    var educationService: EducationCService? = EducationCService(),
    var MonthlyInstallment: Int? = null
)
