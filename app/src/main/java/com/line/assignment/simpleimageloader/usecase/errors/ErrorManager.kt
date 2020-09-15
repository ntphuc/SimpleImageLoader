package com.line.assignment.simpleimageloader.usecase.errors

import com.line.assignment.simpleimageloader.data.error.Error
import com.line.assignment.simpleimageloader.data.error.mapper.ErrorMapper
import javax.inject.Inject

/**
 * Created by PhucNguyen
 */

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorFactory {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
