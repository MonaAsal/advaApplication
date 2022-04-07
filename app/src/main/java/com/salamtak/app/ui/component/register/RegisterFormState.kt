package com.salamtak.app.ui.component.register

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val acceptedTerms: Int? = null,
    val firstnameError: Int? = null,
    val lastnameError: Int? = null,
    val passwordError: String? = null,
    val emailError: String? = null,
    val phoneError: Int? = null,
    val passwordDontMatchError: String? = null,
    val isDataValid: Boolean = false
)
