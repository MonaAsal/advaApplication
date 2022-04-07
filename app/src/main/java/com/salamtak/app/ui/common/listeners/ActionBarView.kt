package com.salamtak.app.ui.common.listeners

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface ActionBarView {

    fun setUpIconVisibility(visible: Boolean)

    fun setTitle(titleKey: String)

    fun setSettingsIconVisibility(visibility: Boolean)

    fun setRefreshVisibility(visibility: Boolean)
}
