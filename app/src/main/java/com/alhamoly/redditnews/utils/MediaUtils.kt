package com.alhamoly.redditnews.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.loadAny
import com.alhamoly.redditnews.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

object MediaUtils {

    fun ImageView.loadUrl(url: String) {

        if (url.contains("svg", ignoreCase = true)) {
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry {
                    add(SvgDecoder(this@loadUrl.context))
                }
                .build()

            this.loadAny(url, imageLoader)
        } else
            this.load(url)

    }

    fun String.requestBody(): RequestBody =
        this.toRequestBody("multipart/form-data".toMediaTypeOrNull())

    @SuppressLint("Recycle")
    fun imageBody(mContext: Context, uri: Uri, key: String? = null): MultipartBody.Part {
        val p: String
        val cursor = mContext.contentResolver.query(uri, null, null, null, null)

        p = if (cursor == null) {
            uri.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }

        val file = File(p)
        val requestFile =file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            if (key.isNullOrEmpty()) Constants.IMAGE else key,
            file.name,
            requestFile
        )
    }

    private fun byteArrayFromBitmap(bitmap: Bitmap): ByteArray {

        var imageInByte: ByteArray = byteArrayOf()
        return try {
            val mByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mByteArrayOutputStream)
            imageInByte = mByteArrayOutputStream.toByteArray()
            imageInByte
        } catch (e: Exception) {
            imageInByte

        }
    }

    fun convertVideoToBytes(uri: Uri): ByteArray {
        var videoBytes = byteArrayOf()
        try { //  w  w w  . j ava 2s . c  o m
            val baos = ByteArrayOutputStream()
            val fis = FileInputStream(File(uri.toString()))
            val buf = ByteArray(1024)
            var n: Int
            while (-1 != fis.read(buf).also { n = it }) baos.write(buf, 0, n)
            videoBytes = baos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return videoBytes
    }


    @SuppressLint("Recycle")
    fun Uri.videoBody(mContext: Context, key: String? = null): MultipartBody.Part {
        val p: String
        val cursor = mContext.contentResolver.query(this, null, null, null, null)

        p = if (cursor == null) {
            this.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            cursor.getString(idx)
        }

        val file = File(p)
        val requestFile =file.asRequestBody("video/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            if (key.isNullOrEmpty()) "video" else key,
            file.name,
            requestFile
        )
    }

    fun byteArrayFromImageView(imageView: ImageView): ByteArray {

        var imageInByte: ByteArray = byteArrayOf()
        return try {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            imageInByte = baos.toByteArray()
            imageInByte
        } catch (e: Exception) {
            imageInByte

        }
    }

}