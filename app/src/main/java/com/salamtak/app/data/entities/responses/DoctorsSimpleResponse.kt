package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.DoctorBase

data class DoctorsSimpleResponse(
    val `data`: List<DoctorBase>
):BasePagingResponse()