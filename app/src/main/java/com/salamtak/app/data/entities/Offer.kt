package com.salamtak.app.data.entities

data class Offer(
    val providerName: String,
    val desc: String,
    val id: String,
    val discountAmount: Double,
    val serviceFees: Double,
    val service: Service
)

data class Service(val id: String, val name: String)