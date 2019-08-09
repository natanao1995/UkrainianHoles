package com.example.ukrainianholes.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.*
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    fun getMimeType(context: Context, uri: Uri): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri.toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase()
            )
        }
    }

    fun getMimeType(context: Context, filePath: String): String? {
        val uri = Uri.fromFile(File(filePath)) ?: return null
        return getMimeType(context, uri)
    }

    fun indexOfExtension(filename: String?): Int {
        return if (filename == null) {
            -1
        } else {
            val extensionPos = filename.lastIndexOf('.')
            val lastSeparator = indexOfLastSeparator(filename)
            if (lastSeparator > extensionPos) -1 else extensionPos
        }
    }

    fun getFileExtension(file: File): String? {
        var extension: String? = null
        val filename = file.name
        val index = filename.lastIndexOf('.')
        if (index >= 0) {
            extension = filename.substring(index + 1)
        }
        return extension
    }

    fun indexOfLastSeparator(filename: String?): Int {
        if (filename == null) {
            return -1
        } else {
            val lastUnixPos = filename.lastIndexOf('/')
            val lastWindowsPos = filename.lastIndexOf('\\')
            return Math.max(lastUnixPos, lastWindowsPos)
        }
    }

    fun removeExtension(filename: String?): String? {
        return if (filename == null) {
            null
        } else {
            val index = indexOfExtension(filename)
            if (index == -1) filename else filename.substring(0, index)
        }
    }

    /**
     * @param src    Source file.
     * @param target Target file.
     * @return Copied file.
     */
    @Throws(IOException::class)
    fun copyFile(src: File, target: File): File {

        //if folder does not exist

        target.createNewFile()
        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null

        try {
            inChannel = FileInputStream(src).channel
            outChannel = FileOutputStream(target).channel
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        try {
            inChannel!!.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel?.close()
            outChannel?.close()
        }

        return target
    }

    private fun externalMemoryAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    val availableExternalMemorySizeInKb: Long
        get() {
            if (externalMemoryAvailable()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize = stat.blockSizeLong
                val availableBlocks = stat.availableBlocksLong
                return availableBlocks * blockSize / 1024
            } else {
                return -1
            }
        }

    fun getUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext
                .packageName + ".provider", file
        )
    }

    val currentDateAndTime: String
        get() {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
            return df.format(c.time)
        }

    fun getTempPhotoPath(context: Context): String {
        //TODO: replace cache dir with files dir
        val cacheDir = context.externalCacheDir
        return (cacheDir!!.absolutePath
                + File.separator + "temp_photo" + ".jpg")
    }
}