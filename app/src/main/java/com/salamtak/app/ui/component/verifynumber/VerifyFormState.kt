package com.salamtak.app.ui.component.verifynumber

/**
 * Data validation state of the login form.
 */
data class VerifyFormState(
    val codeError: Int? = null,
    val isDataValid: Boolean = false
)
