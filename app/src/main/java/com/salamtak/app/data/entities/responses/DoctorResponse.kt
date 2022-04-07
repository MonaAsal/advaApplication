package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Doctor

data class DoctorResponse(
    val `data`: Doctor
):BasePagingResponse()