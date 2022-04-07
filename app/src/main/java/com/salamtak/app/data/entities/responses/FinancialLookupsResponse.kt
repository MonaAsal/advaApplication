package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.FinancialLookupData

data class FinancialLookupsResponse(
    val `data`: FinancialLookupData
):BaseResponse()