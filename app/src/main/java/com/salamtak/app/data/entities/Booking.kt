package com.salamtak.app.data.entities

data class Booking(
    val downPayment: Double,
    val id: String,
    val installmentType: InstallmentType,
    val patientId: String,
    val salamtakOperationId: String
)