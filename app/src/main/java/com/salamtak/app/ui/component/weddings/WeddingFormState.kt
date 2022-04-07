package com.salamtak.app.ui.component.weddings

/**
 * Data validation state of the  form.
 */
data class WeddingFormState(

    val nameError: Int? = null,
    val phoneError: Int? = null,
    val companyNameError: Int? = null,
    val inviteesError: Int? = null,
    val monthlyInstallmentError: Int? = null,
    val typeError: Int? = null,
    val downPayment: Int? = null,
    val totalCost: Int? = null,
    val isDataValid: Boolean = false
)
