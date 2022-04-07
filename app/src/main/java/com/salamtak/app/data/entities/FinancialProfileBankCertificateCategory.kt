package com.salamtak.app.data.entities

data class FinancialProfileBankCertificateCategory(
    var id: String? = "",
    var updated: Boolean = false,
    val netIncome: Double?,
    var attachments: MutableList<FinancialTypeAttachments>?
)