package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.FinancialProfile

data class FinancialProfileResponse(
    val `data`: FinancialProfile
):BaseResponse()