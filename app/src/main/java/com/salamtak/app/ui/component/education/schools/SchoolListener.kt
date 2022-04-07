package com.salamtak.app.ui.component.education.schools

import com.salamtak.app.data.entities.EducationSubCategory
import com.salamtak.app.data.entities.School

interface SchoolListener {
    fun onSchoolClicked(school:School)
    fun onSubcategoryClicked(school:EducationSubCategory)
}