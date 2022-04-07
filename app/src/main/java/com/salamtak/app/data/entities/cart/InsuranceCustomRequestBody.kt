package com.salamtak.app.data.entities.cart

data class InsuranceRequestBody(

var type: Int? = null,
var installmentTypeId: Int? = null,
var downPayment: Int? = null,
var totalCost: Int? = null,
var providerId: String? = null,
var cartUID: String? = null,
var itemUID: String? = null,
var deviceId: String? = null,
var custom: InsuranceCustom? = null,
var insuranceService: InsuranceService? = null,
var MonthlyInstallment: Int? = null,


)

data class InsuranceCustom(
    var FullName: String? = null,
    var PhoneNumber: String? = null,
    var ProviderName: String? = null,
    var MonthlyInstallment: Int? = null,
    var AdditionalInfo: String? = null

)

data class InsuranceService(
    var insuranceType: Int? = null,
    var customInsuranceCompany: String? = null
)

