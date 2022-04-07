package com.salamtak.app.ui.component.password

/**
 * Data validation state of the login form.
 */
data class ChangePasswordFormState(
    val oldPasswordError: String? = null,
    val passwordError: String? = null,
    val passwordDontMatchError: String? = null,
    val emailError: Int? = null,
    val isDataValid: Boolean = false
)
