package com.salamtak.app.ui.component.profile.edit

/**
 * Data validation state of the login form.
 */
data class ProfileFormState(
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val phoneError:Int? = null,
    val emailError: String? = null,
    val isDataValid: Boolean = false
)
