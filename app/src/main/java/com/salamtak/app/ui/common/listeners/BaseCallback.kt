package com.salamtak.app.ui.common.listeners

import com.salamtak.app.data.error.Error

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface BaseCallback {
    fun onSuccess(data: Any)

    fun onFail(error : Error)
}
