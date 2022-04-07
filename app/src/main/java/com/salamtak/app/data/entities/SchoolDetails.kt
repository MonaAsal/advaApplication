package com.salamtak.app.data.entities

data class SchoolDetails(
    val categoryId: String,
    val subCategoryId: String,
    val imageUrl: String,
    val branches: List<EducationBranch>
) : School()