package com.salamtak.app.data.entities

data class ProfileLookups(
    val employmentFields: List<EmploymentField>,
    val employmentTypes: List<EmploymentType>,
    val egyptianResidentalStatus: List<ResidentalStatus>,
    val foreignResidentalStatus: List<ResidentalStatus>
)