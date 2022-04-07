package com.salamtak.app.data.entities

class Step2(
    var id: String,
    var clubMembershipEnabled: Boolean,
    var clubId: String?,
    var carOwnerEnabled: Boolean,
    var carId: String?,
    var creditAndLoanEnabled: Boolean,
    var type: String,
    var limit: String,
    val progress: Int
)