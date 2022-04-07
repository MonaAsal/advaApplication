package com.salamtak.app.data.entities

data class FinancialLookupData(
    val bankInfos: List<BankInfo>,
    val carModels: List<CarModel>,
    val clubs: List<Club>,
    val maritalStatus: List<MaritalStatus>
)