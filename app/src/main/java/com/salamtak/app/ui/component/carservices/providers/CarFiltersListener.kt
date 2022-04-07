package com.salamtak.app.ui.component.carservices.providers

import com.salamtak.app.data.entities.Area
import com.salamtak.app.data.entities.CarModel
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.City

interface CarFiltersListener {
    fun onCarBrandsSelected(carModel: List<CarModel>?)
    fun onCarServicesSelected(categories: List<Category>?)
    fun onCarLocationSelected(area:List<Area>?)

}