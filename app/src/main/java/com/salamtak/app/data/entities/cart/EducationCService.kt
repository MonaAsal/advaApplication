package com.salamtak.app.data.entities.cart

import com.salamtak.app.data.entities.BaseStudent
import com.salamtak.app.data.entities.Student

data class EducationCService(
    var educationEntityName: String? = "",
    var branchName: String? = "",
    var SchoolBranchId: String ?= "",
    var eduType: Int? = 0,
    var students: List<Students>? = arrayListOf()
)

data class Students(
    var studentName: String? = "",
    var studentId: String? = "",
    var busFees: Int? = 0,
    var studyFees: Int? = 0,
    var isBusSubscriped: Boolean? = false,
    var customGrade: String? = ""
)

