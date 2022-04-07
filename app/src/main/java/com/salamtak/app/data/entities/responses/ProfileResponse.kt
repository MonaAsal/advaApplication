package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.BaseUser

data class ProfileResponse(
    val `data`: BaseUser?
) : BaseResponse()