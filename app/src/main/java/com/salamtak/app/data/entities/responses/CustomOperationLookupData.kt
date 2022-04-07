package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.InstallmentType

data class CustomOperationLookupData(
    val categories: List<Category>,
    val installmentTypes: List<InstallmentType>
):BaseResponse()