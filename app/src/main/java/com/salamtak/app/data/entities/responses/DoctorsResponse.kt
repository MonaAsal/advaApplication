package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Doctor

data class DoctorsResponse(
    val `data`: List<Doctor>
):BasePagingResponse()