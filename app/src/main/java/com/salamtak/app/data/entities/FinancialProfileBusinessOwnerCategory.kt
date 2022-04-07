package com.salamtak.app.data.entities

data class FinancialProfileBusinessOwnerCategory(
    var id: String? = "",
    var updated: Boolean = false,
    val companyAddress: String?,
    val companyName: String?,
    val companyNetIncome: Double?,
    val job: String?,
    var attachments: MutableList<FinancialTypeAttachments>?
)