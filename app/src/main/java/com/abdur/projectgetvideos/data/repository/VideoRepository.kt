package com.abdur.projectgetvideos.data.repository

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.MutableLiveData
import com.abdur.projectgetvideos.common.Response
import com.abdur.projectgetvideos.data.model.VideoDetail
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoRepository @Inject constructor(@ApplicationContext private val context: Context) {
    val videos = MutableLiveData<Response<List<VideoDetail>>>()

    suspend fun getVideos(): MutableLiveData<Response<List<VideoDetail>>> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA
        )

        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        val videoList = mutableListOf<VideoDetail>()

        cursor?.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumn = c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val uriColumn = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (c.moveToNext()) {
                val id = c.getLong(idColumn)
                val title = c.getString(titleColumn)
                val uri = Uri.parse(c.getString(uriColumn))

                val thumbnailBitmap = getVideoThumbnail(id)
                videoList.add(VideoDetail(id, title, uri, thumbnailBitmap))
            }
        }

        if (videoList.isEmpty()) {
            videos.postValue(Response.Success(emptyList()))
        } else {
            videos.postValue(Response.Success(videoList))
        }

        return@withContext videos
    }


    private fun getVideoThumbnail(videoId: Long): Bitmap? {
        val contentResolver = context.contentResolver
        val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId)
        return if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) {
            contentResolver.loadThumbnail(uri, Size(100, 100), null)
        } else {
            MediaStore.Video.Thumbnails.getThumbnail(
                contentResolver,
                videoId,
                MediaStore.Video.Thumbnails.MINI_KIND,
                null
            )
        }
    }
}