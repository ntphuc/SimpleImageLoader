package com.line.assignment.simpleimageloader.utils

import android.content.Context
import com.google.gson.Gson
import com.line.assignment.simpleimageloader.R
import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem

/**
 * Created by PhucNguyen
 */

class TimeMeasuring {
    private val DELAY_TIME_UPDATES: Int = 100 //ms
    private var startTime: Long = 0
    private var steps: Long = 0

    init {

    }

    fun startMeasure() {
        steps = 0
        startTime = System.currentTimeMillis()
    }

    fun checkIsUpdates(): Boolean {
        var nextSteps: Long = (System.currentTimeMillis() - startTime) / DELAY_TIME_UPDATES
        if (nextSteps > steps) {
            steps = nextSteps
            return true
        }
        return false
    }

}
