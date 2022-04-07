package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.InstallmentType

data class EducationInstallments(
    val inistituteInstallments: List<InstallmentType>,
    val univeristyInstallments: List<InstallmentType>,
    val coursesInstallments: List<InstallmentType>

)