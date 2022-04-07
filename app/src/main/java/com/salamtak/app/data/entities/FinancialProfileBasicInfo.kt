package com.salamtak.app.data.entities

data class FinancialProfileBasicInfo(
    val areaId: Int,
    val building: String,
    val cityId: Int,
    val creditCardLimit: Int,
    val familyName: String,
    val financialProviderId: String,
    val firstName: String,
    val id: String,
    val idBackUrl: String,
    val idFaceUrl: String,
    val idNumber: String,
    val isDraft: Boolean,
    val lastName: String,
    val loanMonthlyAmount: Int,
    val maritalStatusId: Int,
    val middleName: String,
    val mobile: String,
    val nationalIDDate: String,
    val paymentCard: Int,
    val streetAddress: String
)