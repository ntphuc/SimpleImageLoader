package com.line.assignment.simpleimageloader.data.error.mapper

import com.line.assignment.simpleimageloader.App
import com.line.assignment.simpleimageloader.R
import com.line.assignment.simpleimageloader.data.error.*
import javax.inject.Inject

class ErrorMapper @Inject constructor() : ErrorMapperInterface {

    override fun getErrorString(errorId: Int): String {
        return App.context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
                Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
                Pair(NETWORK_ERROR, getErrorString(R.string.network_error)),
                Pair(NO_DATA_ERROR, getErrorString(R.string.no_data))
        ).withDefault { getErrorString(R.string.network_error) }
}
