package com.salamtak.app.ui.component.finishing.providers

import com.salamtak.app.data.entities.FinishingProvider
import com.salamtak.app.data.entities.GenericProvider


interface FinishingProviderListener{
    fun onProviderClicked(provider: FinishingProvider)
}