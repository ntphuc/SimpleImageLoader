package com.line.assignment.simpleimageloader.data.dto.download

import android.graphics.Bitmap

sealed class DownloadResult {
    data class Success(val bitmap:Bitmap?) : DownloadResult()

    data class Error(val message: String, val cause: Exception? = null) : DownloadResult()

    data class Progress(val progress: Long): DownloadResult()
}
