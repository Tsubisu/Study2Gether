package com.example.studygether.Repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.studygether.BuildConfig

class CloudinaryImageRepositoryImpl : ImageRepository {

    private fun ensureInitialized(context: Context) {
        try {
            MediaManager.get()
        } catch (e: IllegalStateException) {
            val config = mapOf(
                "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
                "secure" to true
            )
            MediaManager.init(context.applicationContext, config)
        }
    }

    override fun uploadImage(context: Context, imageUri: Uri, callback: (UploadResult?) -> Unit) {
        ensureInitialized(context)

        MediaManager.get().upload(imageUri)
            .option("resource_type", "image")
            .option("upload_preset", BuildConfig.CLOUDINARY_UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    Log.d("Cloudinary", "Upload started")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = if (totalBytes > 0) (bytes.toDouble() / totalBytes) else 0.0
                    Log.d("Cloudinary", "Upload progress: $progress")
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String ?: ""
                    val publicId = resultData["public_id"] as? String ?: ""
                    Log.d("Cloudinary", "Upload success: $url")
                    callback(UploadResult(url, publicId))
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Log.e("Cloudinary", "Upload error: ${error.description}")
                    callback(null)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d("Cloudinary", "Upload rescheduled")
                }
            }).dispatch()
    }
}
