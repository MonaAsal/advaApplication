package com.salamtak.app.data.entities.cart

data class FinishingCategoryRequestBody(
    var type: Int? = 0,
    var installmentTypeId: Int? = 0,
    var downPayment: Int? = 0,
    var totalCost: Int? = 0,
    var providerId: String? = "",
    var cartUID: String? = "",
    var itemUID: String? = "",
    var deviceId: String? = "",
    var finishingService: FinishingService? = FinishingService(),
    var monthlyInstallment: Int? = 0,

)



