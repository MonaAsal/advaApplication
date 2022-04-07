package com.salamtak.app.data.entities

data class FinancialProfileEmployeesCategory(
    var id: String? = "",
    val updated: Boolean = false,
    val companyAddress: String?,
    val companyName: String?,
    val job: String?,
    val salary: Double,
    var attachments: MutableList<FinancialTypeAttachments>?
)