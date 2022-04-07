package com.salamtak.app.data.entities

data class OfferHistoryService(
    val discountAmount: Double,
    val id: String,
    val service: Service,
    val serviceFees: Double,
    val medicalProvider: MedicalProviderDetails
)