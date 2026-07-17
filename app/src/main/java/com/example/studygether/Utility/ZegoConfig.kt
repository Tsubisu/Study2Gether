package com.example.studygether.Utility

import android.util.Log

object ZegoConfig {
    
    
    const val APP_ID: Long = 577353141
    const val APP_SIGN: String = "03f3b5175d486d87f6c090fc4134d0d31cf48a7b35c1e37593e827082690f78b"

    fun isPlaceholder(): Boolean {
        return APP_ID == 123456789L || APP_SIGN.startsWith("abcdefgh")
    }

    fun checkConfig() {
        if (isPlaceholder()) {
            Log.w("ZegoConfig", "WARNING: ZEGOCLOUD AppID or AppSign is using placeholder values. Real time Chat and Calling features will fail to connect. Please configure real credentials in ZegoConfig.kt.")
        }
    }
}
