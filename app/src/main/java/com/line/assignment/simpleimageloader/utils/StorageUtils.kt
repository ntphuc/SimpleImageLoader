package com.line.assignment.simpleimageloader.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.core.content.FileProvider
import com.line.assignment.simpleimageloader.BuildConfig
import com.line.assignment.simpleimageloader.LOG_TAG
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

/**
 *
 *
 */
/**
 * Manage Storage Utils
 *
 * @author: PhucNT
 * @version: 1.1
 * @since: Sep 15, 2020
 */
class StorageUtils @Inject constructor(val context: Context) {


    private val PATH_FOLDER_IMAGE: String = "l_cached"
    private val TYPE_CACHE_EXTERNAL = 0
    private val TYPE_CACHE_INTERNAL = 1
    private val MIN_MEMORY_SIZE_CLEAN_CACHE = 50f
    private val IMAGE_CACHE = "images"

    var cacheRootPath: File? = null

    init {
        // Config custom message
        cacheRootPath = initCacheRoot()
    }

    fun getCachedRoot(): File? {
        return cacheRootPath
    }

    /**
     * init Cache root
     *
     * @author: PhucNT
     * @return: void
     * @param: file
     * @throws:
     */
    fun initCacheRoot(): File? {
        var cacheRoot: File? = null
        val rootExternal = getImageCachePath(TYPE_CACHE_EXTERNAL)
        val rootInternal = getImageCachePath(TYPE_CACHE_INTERNAL)
        cacheRoot = if (isSdPresent && availableToUse()) {
            rootExternal
        } else {
            rootInternal
        }
        if (cacheRoot != null) Log.e(LOG_TAG, "thu muc cache root " + cacheRoot.path)
        return cacheRoot
    }

    private fun availableToUse(): Boolean {
        val memAvailExternal = megabytesAvailable(context?.getExternalFilesDir(null));
        Log.e(LOG_TAG, "size external available "
                + memAvailExternal);
        if (memAvailExternal < MIN_MEMORY_SIZE_CLEAN_CACHE) {
            val rootExternal = getImageCachePath(TYPE_CACHE_EXTERNAL)
            if (rootExternal != null && rootExternal.exists())
                CacheCleanTask().execute(rootExternal);
            return false
        }
        return true
    }

    /**
     * create folder image cache by typeCache = External or Internal
     *
     * @author: PhucNT
     * @return: void
     * @throws:
     */
    private fun getImageCachePath(typeCache: Int): File? {
        val cache = initImageCacheFolder(typeCache) ?: return null
        var fileCacheToDisk: File? = File(cache.absolutePath, IMAGE_CACHE)
        if (!fileCacheToDisk!!.exists()) {
            deleteDir(File(cache.absolutePath))
            if (fileCacheToDisk.mkdir()) {
            } else {
                fileCacheToDisk = null
            }
        }
        return fileCacheToDisk
    }

    fun deleteDir(dir: File) {
        try {
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    if (children[i].contains(IMAGE_CACHE)) {
                        CacheCleanTask().execute(File(dir, children[i]))
                        File(dir, children[i]).delete()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get size of folder root
     *
     * @author: PhucNT
     * @return: void
     * @throws:
     */
    fun getFileSize(folder: File?): Long {
        if (folder == null) return 0
        Log.e(LOG_TAG, "Folder: " + folder.name)
        var foldersize: Long = 0
        val filelist = folder.listFiles()
        for (i in filelist.indices) {
            foldersize += if (filelist[i].isDirectory) {
                getFileSize(filelist[i])
            } else {
                filelist[i].length()
            }
        }
        return foldersize
    }


    /**
     * Get free size of folder
     *
     * @author: PhucNT
     * @return: void
     * @param: file
     * @throws:
     */
    fun megabytesAvailable(f: File?): Float {
        val stat = StatFs(f?.path)
        val bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
        return bytesAvailable / (1024f * 1024f)

    }

    val isSdPresent: Boolean
        get() = Environment.getExternalStorageState() ==
                Environment.MEDIA_MOUNTED

    /**
     * Create folder cache
     *
     * @author: PhucNT
     * @param:
     * @return: void
     * @throws:
     */
    protected fun initImageCacheFolder(typeCache: Int): File? {
        var extStorageDirectory: File? = null
        var folderRoot: File? = null
        try {
            if (typeCache == TYPE_CACHE_EXTERNAL) extStorageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if (typeCache == TYPE_CACHE_INTERNAL) extStorageDirectory = context?.filesDir
            folderRoot = File(extStorageDirectory, PATH_FOLDER_IMAGE)

            folderRoot.mkdir()

        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return folderRoot
    }

    fun createFileUri(url: String): Uri {
        val folder = cacheRootPath
        val fileName = md5(url)
        val file = File(folder, fileName)
        val uri = context?.let {
            FileProvider.getUriForFile(it, "${BuildConfig.APPLICATION_ID}.provider", file)
        }
        return uri
    }

    fun getImageFileLocal(url: String): File {
        val folder = cacheRootPath
        val fileName = md5(url)
        val file = File(folder, fileName)

        return file
    }

    fun md5(s: String): String? {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest: MessageDigest = MessageDigest
                .getInstance(MD5)
            digest.update(s.toByteArray())
            val messageDigest: ByteArray = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     *
     * Delete cached
     *
     * @author: PhucNT
     * @return
     * @return: CacheCleanTask
     * @throws:
     */
    val cacheCleanTask: CacheCleanTask
        get() = CacheCleanTask()

     class CacheCleanTask : AsyncTask<File?, Void?, Void?>() {
        override fun doInBackground(vararg params: File?): Void? {
            try {
                params[0]?.let { walkDir(it) }
            } catch (t: Throwable) {
                Log.e(LOG_TAG, "Exception cleaning cache", t)
            }
            return null
        }

        fun walkDir(dir: File) {
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    walkDir(File(dir, children[i]))
                }
            } else {
                dir.delete()
            }
        }
    }
}