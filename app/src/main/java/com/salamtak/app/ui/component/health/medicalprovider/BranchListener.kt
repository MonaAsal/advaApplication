package com.salamtak.app.ui.component.health.medicalprovider

import com.salamtak.app.data.entities.Branch

interface BranchListener {
    fun onMapClicked(branch:Branch)
}