package com.salamtak.app.data.entities

data class OfferHistory(
    val discountCode: String,
    val expireAt: String,
    val id: String,
    val isUsed: Boolean,
    val qrImageUrl: String,
    val service: OfferHistoryService
)