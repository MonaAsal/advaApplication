package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.*

data class FinancialCategoriesData(
    val financialProfileAssetsOwnerCategory: FinancialProfileAssetsOwnerCategory?,
    val financialProfileBankCertificateCategory: FinancialProfileBankCertificateCategory?,
    val financialProfileBusinessOwnerCategory: FinancialProfileBusinessOwnerCategory?,
    val financialProfileEmployeesCategory: FinancialProfileEmployeesCategory?,
    val financialProfilePensionCategory: FinancialProfilePensionCategory?,
    val financialProfileCategoryAttachments: List<FinancialTypeAttachments>?,
    val guideNote: String?
)