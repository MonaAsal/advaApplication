package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.MedicalProfile

data class MedicalProfileResponse(
    val `data`: MedicalProfile
):BaseResponse()