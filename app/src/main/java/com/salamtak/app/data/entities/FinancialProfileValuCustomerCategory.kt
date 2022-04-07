package com.salamtak.app.data.entities

data class FinancialProfileValuCustomerCategory(
    var updated: Boolean = false,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    var attachments: MutableList<FinancialTypeAttachments>?
)