package com.salamtak.app.ui.common.listeners

/**
 * Created by Radwa Elsahn on 3/21/2020
 */


interface RecyclerItemListener<T> {
    fun onItemSelected(item: T)
}

interface RecyclerItemMultiSelectionListener<T> {
    fun onItemMultiSelected(item: T)
    fun onItemDeSelected(item: T)
}

