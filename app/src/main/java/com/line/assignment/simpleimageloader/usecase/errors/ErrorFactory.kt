package com.line.assignment.simpleimageloader.usecase.errors

import com.line.assignment.simpleimageloader.data.error.Error

interface ErrorFactory {
    fun getError(errorCode: Int): Error
}
