package com.salamtak.app.ui.component.financialinfo.step1

/**
 * Data validation state of the login form.
 */
data class Step1FormState(
    val firstNameError: Int? = null,
    val secondNameError: Int? = null,
    val thirdNameError: Int? = null,
    val lastNameError: Int? = null,
    val nationalIdError: Int? = null,
    val nationalIdExpireDateError: Int? = null,
    val passportNumberError: Int? = null,
    val passportExpireDateError: Int? = null,
    val streetAddressError: Int? = null,
    val buildingNumError: Int? = null,
    val imagesError: Int? = null,
    val phoneError: Int? = null,
    val paymentError: Int? = null,


    val firstNameErrorGuarantor: Int? = null,
    val secondNameErrorGuarantor: Int? = null,
    val thirdNameErrorGuarantor: Int? = null,
    val lastNameErrorGuarantor: Int? = null,
    val nationalIdErrorGuarantor: Int? = null,
    val nationalIdExpireDateErrorGuarantor: Int? = null,
    val passportNumberErrorGuarantor: Int? = null,
    val passportExpireDateErrorGuarantor: Int? = null,
    val imagesErrorGuarantor: Int? = null,
    val phoneErrorGuarantor: Int? = null,


    val isDataValid: Boolean = false


)
