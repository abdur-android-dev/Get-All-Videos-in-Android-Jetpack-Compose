package com.abdur.projectgetvideos.data.model

import android.graphics.Bitmap
import android.net.Uri

data class VideoDetail (
    val id: Long,
    val title: String,
    val uri: Uri,
    val thumbnailBitmap: Bitmap?
)