package com.line.assignment.simpleimageloader.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.line.assignment.simpleimageloader.BuildConfig
import com.line.assignment.simpleimageloader.data.dto.download.DownloadResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*
import java.lang.ref.WeakReference
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject


class SimpleImageDownloader @Inject constructor(val context: Context) {


    private var mStorageUtils: StorageUtils = StorageUtils(context)
    private var mTimeMeasuring: TimeMeasuring = TimeMeasuring()

    fun displayImage(url: String, imageView: ImageView): Flow<DownloadResult> {
        return if (isExistLocal(url)) {
            loadImageLocal(url, imageView)
        } else {
            loadImageNetwork(url, imageView)
        }

        //   return loadImageFromNetwork(url, imageView)
    }

    private fun isExistLocal(url: String): Boolean {

        val fileImage: File = mStorageUtils.getImageFileLocal(url)
        return fileImage.exists()
    }


    private fun loadImageLocal(url: String, imageView: ImageView): Flow<DownloadResult> {
        val file: File = mStorageUtils.getImageFileLocal(url)
        return flow {
            if (imageView.width == 0 || imageView.height == 0) {
                //waiting for render image view before decode
                delay(200)
            }
            val weakBitmap: WeakReference<Bitmap>? =
                BitmapUtils.decodeSampledBitmapFromFile(file, imageView)
            emit(DownloadResult.Success(weakBitmap?.get()))
            if (weakBitmap?.get() == null) {
                emit(DownloadResult.Error("bitmap null"))
            } else {
                emit(DownloadResult.Success(weakBitmap.get()))
            }

        }
    }

    fun loadImageNetwork(url: String, imageView: ImageView): Flow<DownloadResult> {
        val file: File = mStorageUtils.getImageFileLocal(url)
        return flow {
            var inputStream: InputStream? = null
            val outputStream = FileOutputStream(file)
            try {
                val response: Response = handleRequestUrl(url)
                var downloaded: Long = 0
                if (response.body != null) {

                    inputStream = response.body?.byteStream()
                    val buff = ByteArray(1024 * 4)

                    mTimeMeasuring.startMeasure()

                    while (true) {

                        val readed: Int = inputStream!!.read(buff)
                        if (readed == -1) {
                            break
                        }
                        //write buff
                        downloaded += readed.toLong()

                        // save to local
                        withContext(Dispatchers.IO) {
                            outputStream.write(buff, 0, readed)
                        }

                        //  val progress = (downloaded * 100f / target!!).roundToInt()
                        val kb_downloaded = (downloaded / 1024)

                        // check time 100ms
                        if (mTimeMeasuring.checkIsUpdates())
                            emit(DownloadResult.Progress(kb_downloaded))
                    }

                    val weakBitmap: WeakReference<Bitmap>? =
                        BitmapUtils.decodeSampledBitmapFromFile(file, imageView)
                    emit(DownloadResult.Success(weakBitmap?.get()))

                } else {
                    emit(DownloadResult.Error("Failed to connect"))
                }
            } catch (e: IOException) {
                emit(DownloadResult.Error("File not found", e))
            } catch (e: TimeoutCancellationException) {
                emit(DownloadResult.Error("Connection timed out", e))
            } catch (t: Throwable) {
                emit(DownloadResult.Error("Failed to connect"))
            } finally {
                inputStream?.close()
                outputStream?.flush()
                outputStream?.close()
            }

        }
    }

    @Throws(Throwable::class, TimeoutCancellationException::class)
    fun handleRequestUrl(url: String): Response {
        val okHttpClient = OkHttpClient()
        var request = Request.Builder().url(url).build()
        val call: Call = okHttpClient.newCall(request)
        val response: Response = call.execute()
        return response
    }


}
