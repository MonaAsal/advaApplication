package com.salamtak.app.data.entities

data class FinancialProfilePensionCategory(
    var id: String? = "",
    val updated: Boolean = false,
    val pensionNetAmount: Double?,
    var attachments: MutableList<FinancialTypeAttachments>?
)