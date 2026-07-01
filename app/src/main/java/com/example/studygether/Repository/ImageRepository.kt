package com.example.studygether.Repository

import android.content.Context
import android.net.Uri

data class UploadResult(
    val url: String,
    val publicId: String
)

interface ImageRepository {
    fun uploadImage(context: Context, imageUri: Uri, callback: (UploadResult?) -> Unit)

}
