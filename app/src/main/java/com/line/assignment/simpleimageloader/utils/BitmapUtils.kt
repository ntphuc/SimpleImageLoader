package com.line.assignment.simpleimageloader.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.line.assignment.simpleimageloader.LOG_TAG
import com.line.assignment.simpleimageloader.R
import java.io.File
import java.io.InputStream
import java.lang.ref.WeakReference


/**
 * Created by PhucNguyen on 14/10/2017.
 */

object BitmapUtils {


    fun decodeSampledBitmapFromFile(file: File, imageView: ImageView): WeakReference<Bitmap>? {
        var bitmap: WeakReference<Bitmap>? = null
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.inSampleSize = calculateInSampleSize(options, imageView.width, imageView.height)
            Log.e(LOG_TAG, "sample size = " + options.inSampleSize)
            options.inJustDecodeBounds = false
            bitmap = WeakReference(BitmapFactory.decodeFile(file.absolutePath, options))
        } catch (e: Exception) {
        }
        return bitmap
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        Log.e(
            LOG_TAG,
            "reqWidth = " + reqWidth + "reqHeight = " + reqHeight + " width =" + width + " height =" + height
        )
        if ((reqWidth > 0 && reqHeight > 0) && (height > reqHeight || width > reqWidth)) {

            // Calculate ratios of height and width to requested height and width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }




}
