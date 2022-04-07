package com.salamtak.app.ui.common.listeners

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface RecyclerItemPositionListener<T> {
    fun onItemSelected(item: T, position: Int)
}

