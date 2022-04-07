package com.salamtak.app.ui.component.insurance

/**
 * Data validation state of the  form.
 */
data class InsuranceFormState(

    val nameError: Int? = null,
    val phoneError: Int? = null,
    val companyNameError: Int? = null,
    val monthlyInstallmentError: Int? = null,
    val typeError: Int? = null,
    val downPayment: Int? = null,
    val totalCost: Int? = null,
    val isDataValid: Boolean = false
)
