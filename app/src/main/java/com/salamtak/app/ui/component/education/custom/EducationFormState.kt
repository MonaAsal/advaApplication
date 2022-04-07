package com.salamtak.app.ui.component.education.custom

/**
 * Data validation state of the  form.
 */
data class EducationFormState(

    val nameError: Int? = null,
    val nameError2: Int? = null,
    val nameError3: Int? = null,
    val phoneError: Int? = null,
    val companyNameError: Int? = null,
    val monthlyInstallmentError: Int? = null,
    val typeError: Int? = null,
    val downPayment: Int? = null,
    val totalCost: Int? = null,
    val branchNameError: Int? = null,
    val studentNameError:Int? =null,
    val gradeError:Int? =null,
    val gradeError2:Int? =null,
    val gradeError3:Int? =null,
    val educationFeesError:Int? =null,
    val busFeesError:Int? =null,
    val schoolError:Int? =null,
    val isDataValid: Boolean = false
)
