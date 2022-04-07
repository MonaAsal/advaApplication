package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.HomeVisit

data class AddHomeVisitResponse(
    val `data`: HomeVisit
) : BaseResponse()