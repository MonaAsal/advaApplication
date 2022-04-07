package com.salamtak.app.data.entities

data class FilterData(
    val providers: MutableList<MedicalProvider>,
    var cities: MutableList<City>,
    var categoriesAndSubCategories: MutableList<Category>?,
    var subCategories: List<SubCategory>?,
    val startPrice: String,
    val endPrice: String
)
