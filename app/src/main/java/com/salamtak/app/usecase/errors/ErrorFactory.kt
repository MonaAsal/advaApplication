package com.salamtak.app.usecase.errors

import com.salamtak.app.data.error.Error

interface ErrorFactory {
    fun getError(errorCode: Int): Error
}