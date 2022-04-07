package com.salamtak.app.data.entities

data class TransactionDetails(
    val amount: Double,
    val response: String,
    val reverseStatus: String,
    val status: String,
    val transactionDate: String
)