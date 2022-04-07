package com.salamtak.app.data.entities.cart

data class EducationCustomRequestBody(
    var type: Int? = null,
    var installmentTypeId: Int? = null,
    var downPayment: Int? = null,
    var totalCost: Int? = null,
    var providerId: String? = null,
    var cartUID: String? = null,
    var itemUID: String? = null,
    var deviceId: String? = null,
    var custom: EducationCustom? = EducationCustom(),
    var educationService: EducationCService? = EducationCService(),
    var monthlyInstallment: Int? = null
)

data class EducationCustom(
    var fullName: String? = null,
    var phoneNumber: String? = null,
    var providerName: String? = null,
    var monthlyInstallment: Int? = null,
    var additionalInfo: String? = null
)


