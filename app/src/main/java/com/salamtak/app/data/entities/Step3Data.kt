package com.salamtak.app.data.entities

data class Step3Data(
    var id: String = "",
    var progress: Int = 0,
    var employees: FinancialProfileEmployeesCategory?,
    var assetsOwner: FinancialProfileAssetsOwnerCategory?,
    var bankCertificate: FinancialProfileBankCertificateCategory?,
    var businessOwner: FinancialProfileBusinessOwnerCategory?,
    var pension: FinancialProfilePensionCategory?,
    var valuCustomer:FinancialProfileValuCustomerCategory?
)