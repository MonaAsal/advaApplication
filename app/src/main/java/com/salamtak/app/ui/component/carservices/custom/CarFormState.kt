package com.salamtak.app.ui.component.carservices.custom

/**
 * Data validation state of the  form.
 */
data class CarFormState(

    val nameError: Int? = null,
    val phoneError: Int? = null,
    val providerNameError: Int? = null,
    val monthlyInstallmentError: Int? = null,
    val typeError: Int? = null,
    val downPayment: Int? = null,
    val totalCostError: Int? = null,
    val carBrandError: Int? = null,
    val cityError: Int? = null,
    val areaError: Int? = null,
    val isDataValid: Boolean = false
)
