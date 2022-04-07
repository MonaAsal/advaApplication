package com.salamtak.app.data.entities

data class PreFinancialProfile(
    val financialProfile: FinancialProfileBasicInfo?,
    val financialProviders: List<FinancialProvider>,
    val guideNotes: List<GuideNote>,
    val martialStatues: List<MaritalStatus>
)