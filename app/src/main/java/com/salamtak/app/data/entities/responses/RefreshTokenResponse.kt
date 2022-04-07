package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.User

data class RefreshTokenResponse(
    val `data`: User
):BaseResponse()