package com.salamtak.app.data.entities

data class PaymentMethodCard(
    val id: String,
    var isDefault: Boolean,
    val maskedPan: String,
    val subtype: String
)