package com.salamtak.app.data.entities.cart

class FinishingCustomRequestBody(

    var type: Int? = 0,
    var installmentTypeId: Int? = 0,
    var downPayment: Int? = 0,
    var totalCost: Int? = 0,
    var providerId: String? = "",
    var cartUID: String? = "",
    var itemUID: String? = "",
    var deviceId: String? = "",
    var custom: FinishingCustom? = FinishingCustom(),
    var finishingService: FinishingService? = FinishingService(),
    var monthlyInstallment: Int? = null,

)

data class FinishingCustom(

    var fullName: String? = null,
    var phoneNumber: String? = null,
    var providerName: String? = null,
    var providerPhoneNumber: String? = null,
    var monthlyInstallment: Int? = null,
    var additionalInfo: String? = null
)

