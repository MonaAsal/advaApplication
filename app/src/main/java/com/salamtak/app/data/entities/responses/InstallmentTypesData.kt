package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.InstallmentType

data class InstallmentTypesData(
    val educationInstallments: EducationInstallments?,
    val healthInstallments: List<InstallmentType>?,
    val insuranceInstallments: List<InstallmentType>?,
    val weddingInstallments: List<InstallmentType>?,
    val finishingInstallments:List<InstallmentType>?,
    val carServiceInstallments:List<InstallmentType>?
)