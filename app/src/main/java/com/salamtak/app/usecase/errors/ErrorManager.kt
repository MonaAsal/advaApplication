package com.salamtak.app.usecase.errors

import com.salamtak.app.data.error.Error
import com.salamtak.app.data.error.mapper.ErrorMapper
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorFactory {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }

}