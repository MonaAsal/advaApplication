package com.salamtak.app.ui.component.finishing.custom

/**
 * Data validation state of the  form.
 */
data class FinishingFormState(

    val nameError: Int? = null,
    val phoneError: Int? = null,
    val providerNameError: Int? = null,
    val monthlyInstallmentError: Int? = null,
    val typeError: Int? = null,
    val downPayment: Int? = null,
    val totalCostError: Int? = null,
    val isDataValid: Boolean = false
)
