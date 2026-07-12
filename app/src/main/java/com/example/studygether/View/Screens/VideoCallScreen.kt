package com.example.studygether.View.Screens

import android.view.TextureView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.studygether.Utility.ZegoService
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.entity.ZegoCanvas
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.ZegoUpdateType
import im.zego.zegoexpress.entity.ZegoStream
import org.json.JSONObject
import java.util.ArrayList
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.View.AppBars.BottomBars

@Composable
fun VideoCallScreen(
    roomId: String,
    targetUserId: String,
    onCallEnded: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity
    val mainViewModel: AppBarsViewModel = viewModel(activity)

    var isMicEnabled by remember { mutableStateOf(true) }
    var isCameraEnabled by remember { mutableStateOf(true) }

    val localTextureView = remember { TextureView(context) }
    val remoteTextureView = remember { TextureView(context) }

    var hasPermissions by remember {
        mutableStateOf(
            context.checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[android.Manifest.permission.CAMERA] ?: false
        val audioGranted = permissions[android.Manifest.permission.RECORD_AUDIO] ?: false
        hasPermissions = cameraGranted && audioGranted
        if (!hasPermissions) {
            android.widget.Toast.makeText(context, "Camera and microphone permissions are required for video calls", android.widget.Toast.LENGTH_LONG).show()
            onCallEnded()
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.hideTopBar()
        mainViewModel.setBottomBarType(BottomBarState(BottomBars.None))
        if (!hasPermissions) {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.RECORD_AUDIO
                )
            )
        }
    }

    var isRemoteSurfaceReady by remember { mutableStateOf(false) }
    var remoteStreamId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(hasPermissions) {
        if (hasPermissions) {
            localTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {
                    try {
                        ZegoExpressEngine.getEngine().startPreview(ZegoCanvas(localTextureView))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                override fun onSurfaceTextureSizeChanged(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {}
                override fun onSurfaceTextureDestroyed(surface: android.graphics.SurfaceTexture): Boolean {
                    try {
                        ZegoExpressEngine.getEngine().stopPreview()
                    } catch (e: Exception) {}
                    return true
                }
                override fun onSurfaceTextureUpdated(surface: android.graphics.SurfaceTexture) {}
            }

            remoteTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {
                    isRemoteSurfaceReady = true
                    try {
                        val express = ZegoExpressEngine.getEngine()
                        val streamId = remoteStreamId ?: targetUserId
                        express.startPlayingStream(streamId, ZegoCanvas(remoteTextureView))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                override fun onSurfaceTextureSizeChanged(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {}
                override fun onSurfaceTextureDestroyed(surface: android.graphics.SurfaceTexture): Boolean {
                    isRemoteSurfaceReady = false
                    try {
                        val streamId = remoteStreamId ?: targetUserId
                        ZegoExpressEngine.getEngine().stopPlayingStream(streamId)
                    } catch (e: Exception) {}
                    return true
                }
                override fun onSurfaceTextureUpdated(surface: android.graphics.SurfaceTexture) {}
            }

            // Fallback: If surfaces are already available upon composition, trigger manually
            if (localTextureView.isAvailable) {
                try { ZegoExpressEngine.getEngine().startPreview(ZegoCanvas(localTextureView)) } catch (e: Exception) {}
            }
            if (remoteTextureView.isAvailable) {
                isRemoteSurfaceReady = true
                try {
                    val streamId = remoteStreamId ?: targetUserId
                    ZegoExpressEngine.getEngine().startPlayingStream(streamId, ZegoCanvas(remoteTextureView))
                } catch (e: Exception) {}
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                localTextureView.surfaceTextureListener = null
                remoteTextureView.surfaceTextureListener = null
                ZegoService.endRtcCall()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(hasPermissions) {
        val eventHandler = object : IZegoEventHandler() {
            override fun onRoomStreamUpdate(
                roomID: String?,
                updateType: ZegoUpdateType?,
                streamList: ArrayList<ZegoStream>?,
                extendedData: JSONObject?
            ) {
                val express = try { ZegoExpressEngine.getEngine() } catch (e: Exception) { null } ?: return
                if (updateType == ZegoUpdateType.ADD && streamList != null) {
                    for (stream in streamList) {
                        if (stream.user.userID == targetUserId) {
                            remoteStreamId = stream.streamID
                            if (isRemoteSurfaceReady) {
                                express.startPlayingStream(stream.streamID, ZegoCanvas(remoteTextureView))
                            }
                        }
                    }
                } else if (updateType == ZegoUpdateType.DELETE && streamList != null) {
                    for (stream in streamList) {
                        if (stream.user.userID == targetUserId) {
                            onCallEnded()
                        }
                    }
                }
            }
        }

        if (hasPermissions) {
            try {
                val express = ZegoExpressEngine.getEngine()
                express.setEventHandler(eventHandler)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        onDispose {
            try {
                val express = try { ZegoExpressEngine.getEngine() } catch (e: Exception) { null }
                express?.setEventHandler(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Remote stream
        AndroidView(
            factory = { remoteTextureView },
            modifier = Modifier.fillMaxSize()
        )

        // Local Preview
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 110.dp, height = 160.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray)
        ) {
            AndroidView(
                factory = { localTextureView },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Action controls
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .wrapContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        isMicEnabled = !isMicEnabled
                        try {
                            ZegoExpressEngine.getEngine().muteMicrophone(!isMicEnabled)
                        } catch (e: Exception) {}
                    },
                    modifier = Modifier.size(50.dp).background(if (isMicEnabled) Color.DarkGray else Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isMicEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        onCallEnded()
                    },
                    modifier = Modifier.size(60.dp).background(Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = {
                        isCameraEnabled = !isCameraEnabled
                        try {
                            ZegoExpressEngine.getEngine().enableCamera(isCameraEnabled)
                        } catch (e: Exception) {}
                    },
                    modifier = Modifier.size(50.dp).background(if (isCameraEnabled) Color.DarkGray else Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isCameraEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Camera Toggle",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
