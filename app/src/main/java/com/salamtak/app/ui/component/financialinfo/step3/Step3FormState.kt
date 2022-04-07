package com.salamtak.app.ui.component.financialinfo.step3

/**
 * Data validation state of the login form.
 */
data class Step3FormState(
    val jobError: Int? = null,
    val incomeError: Int? = null,
    val companyNameError: Int? = null,
    val companyAddressError: Int? = null,

    val pensionLimitError: Int? = null,
    val assetsOwnerLimitError: Int? = null,
    val bankCertificateAmountError: Int? = null,
    val businessOwnerIncomeError: Int? = null,
    val businessOwnerCompanyAddressError: Int? = null,
    val businessOwnerCompanyNameError: Int? = null,
    val valuLimitError: Int? = null,
    val valuPhoneError: Int? = null,
    val valuMailError: Int? = null,


    val isDataValid: Boolean = false


)
