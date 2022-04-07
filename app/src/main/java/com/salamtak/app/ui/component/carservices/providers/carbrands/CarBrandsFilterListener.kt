package com.salamtak.app.ui.component.carservices.providers.carbrands

import com.salamtak.app.data.entities.CarModel

interface CarBrandsFilterListener {
    fun onCarBrandsSelected(brands: List<CarModel>?)
}