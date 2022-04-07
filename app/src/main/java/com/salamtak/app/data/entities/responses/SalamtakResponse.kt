package com.salamtak.app.data.entities.responses

data class SalamtakResponse<T>(
    val data: T? = null
) : BaseResponse()