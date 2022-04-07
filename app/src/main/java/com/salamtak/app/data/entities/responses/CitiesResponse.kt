package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.City

data class CitiesResponse(
    val `data`: List<City>
):BaseResponse()