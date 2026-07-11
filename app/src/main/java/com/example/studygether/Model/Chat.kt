package com.example.studygether.Model

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import java.util.Date

sealed interface Message {
    val id: String
    val senderId: String
    val timestamp: String
    val isRead: Boolean

}

interface Playable {
    val mediaUrl: String
    val durationSeconds: Int
}

interface Downloadable {
    val fileUrl: String
    val fileName: String
    val fileSizeBytes: Long
    val mimeType: String
}


data class TextMessage(
    override val id: String,
    override val senderId: String,
    override val timestamp:String,
    override val isRead: Boolean,
    val content: String,
) : Message

data class AudioMessage(
    override val id: String,
    override val senderId: String,
    override val timestamp: String,
    override val isRead: Boolean,
    override val mediaUrl: String,
    override val durationSeconds: Int,
) : Message, Playable

data class FileMessage(
    override val id: String,
    override val senderId: String,
    override val timestamp: String,
    override val isRead: Boolean,
    override val fileUrl: String,
    override val fileName: String,
    override val fileSizeBytes: Long,
    override val mimeType: String,
) : Message, Downloadable

data class VideoCallMessage(
    override val id: String,
    override val senderId: String,
    override val timestamp: String,
    override val isRead: Boolean,
    val callDurationSeconds: Int?,
    val isIncoming: Boolean,
) : Message


