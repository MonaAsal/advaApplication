package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.PreferedTime

data class PreferredTimesResponse(
    val `data`: List<PreferedTime>
):BaseResponse()