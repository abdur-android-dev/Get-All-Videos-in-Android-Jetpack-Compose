package com.abdur.projectgetvideos.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdur.projectgetvideos.common.Response
import com.abdur.projectgetvideos.data.model.VideoDetail
import com.abdur.projectgetvideos.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val repository: VideoRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            repository.getVideos()
        }
    }

    val videos: MutableLiveData<Response<List<VideoDetail>>> get() = repository.videos

}
