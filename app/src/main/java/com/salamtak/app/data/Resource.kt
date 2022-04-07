package com.salamtak.app.data

import com.salamtak.app.data.entities.responses.ErrorResponse

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val errorCode: Int? = null,
    val errorResponse: ErrorResponse? = null
) {

    class Success<T>(
        data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class NetworkError<T>(errorCode: Int) : Resource<T>(null, errorCode)
    class DataError<T>(errorResponse: ErrorResponse) : Resource<T>(null,null, errorResponse)
}