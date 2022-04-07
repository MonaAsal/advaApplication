package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.ChronicDiseaseData

data class ChronicDiseasesResponse(
    val `data`: List<ChronicDiseaseData>
):BaseResponse()