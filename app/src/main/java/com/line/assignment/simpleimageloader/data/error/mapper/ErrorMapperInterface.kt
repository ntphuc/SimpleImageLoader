package com.line.assignment.simpleimageloader.data.error.mapper

interface ErrorMapperInterface {
    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
}
