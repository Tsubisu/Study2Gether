package com.example.studygether.ui.theme.tokens

import androidx.compose.ui.unit.sp

object FontSize {
    val micro     = 10.sp   // badges, labels
    val tiny      = 12.sp   // timestamps, captions
    val small     = 14.sp   // secondary body
    val body      = 16.sp   // primary body
    val subtitle  = 18.sp   // subtitles
    val title     = 22.sp   // screen titles
    val headline  = 28.sp   // headings
    val display   = 36.sp   // hero text
}

object LineHeight {
    val tight     = 16.sp   // pairs with micro / tiny
    val normal    = 22.sp   // pairs with small / body
    val relaxed   = 28.sp   // pairs with subtitle / title
    val loose     = 38.sp   // pairs with headline / display
}