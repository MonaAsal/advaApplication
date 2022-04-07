package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.ErrorModel

data class ActionResponse(
    val `data`: String,
    val errors: List<ErrorModel>? = null
):BaseResponse()

