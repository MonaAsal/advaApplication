package com.salamtak.app.ui.component.financialinfo.step2

/**
 * Data validation state of the login form.
 */
data class Step2FormState(
    val carError: Int? = null,
    val clubError: Int? = null,
    val typeError: Int? = null,
    val limitError: Int? = null,
    val carImage1Error: Int? = null,
    val carImage2Error: Int? = null,
    val clubImage1Error: Int? = null,
    val clubImage2Error: Int? = null,

    val isDataValid: Boolean = false


)
