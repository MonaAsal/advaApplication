package com.salamtak.app.ui.component.health.customoperations

/**
 * Data validation state of the  form.
 */
data class CustomOperationFormState(

    val doctorNameError: Int? = null,
    val specialityError: Int? = null,
    val operationName: Int? = null,
    val monthlyInstallment: Int? = null,
    val downPayment: Int? = null,
    val totalCost: Int? = null,
    val isDataValid: Boolean = false
)
