package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.PaymentMethodCard

data class PaymentMethodsResponse(
    val `data`: List<PaymentMethodCard>
):BaseResponse()