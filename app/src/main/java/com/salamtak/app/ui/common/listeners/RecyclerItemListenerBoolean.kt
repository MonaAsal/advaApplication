package com.salamtak.app.ui.common.listeners

/**
 * Created by Radwa Elsahn on 7/16/2020
 */


interface RecyclerItemListenerBoolean<T> {
    fun onItemSelected(item: T, position: Int, flag: Boolean)
}

