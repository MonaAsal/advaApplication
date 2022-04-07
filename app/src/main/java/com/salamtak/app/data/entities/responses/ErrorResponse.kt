package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.ErrorModel

data class ErrorResponse (
    var errors: List<ErrorModel>? = null
):BaseResponse()