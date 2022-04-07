package com.salamtak.app.data.entities.responses


data class GetCartResponse(
    val `data`: List<CartItem>
):BasePagingResponse()