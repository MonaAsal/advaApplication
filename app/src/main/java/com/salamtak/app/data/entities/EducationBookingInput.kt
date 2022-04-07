package com.salamtak.app.data.entities

data class EducationBookingInput(
    val DownPayment: String,
    val EduType: String,
    val InstallmentTypeId: String,
    val Phone: String,
    val Students: List<Student>?,
    val branchName: String,
    val educationEntityName: String,
    val fullName: String,
    val monthlyInstallment: String,
    val totalFees: String
)