package com.salamtak.app.ui.component.carservices.providers.carlocation

import com.salamtak.app.data.entities.Area

interface CarAreasFilterListener {
    fun onCarLocationSelected(area: List<Area>?)

}