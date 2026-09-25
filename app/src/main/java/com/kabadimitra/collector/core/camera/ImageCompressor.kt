package com.kabadimitra.collector.core.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageCompressor {

    fun compressImage(
        sourceFile: File,
        targetFile: File,
        maxSizeBytes: Long = 100 * 1024 // target ~100 KB
    ): File {
        val bitmap = BitmapFactory.decodeFile(sourceFile.absolutePath) ?: return sourceFile

        var quality = 80
        var stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

        while (stream.size() > maxSizeBytes && quality > 20) {
            stream.reset()
            quality -= 15
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        }

        FileOutputStream(targetFile).use { out ->
            out.write(stream.toByteArray())
        }

        return targetFile
    }
}
