package com.abdur.projectgetvideos.ui.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import coil.compose.AsyncImage
import com.abdur.projectgetvideos.R
import com.abdur.projectgetvideos.ui.viewmodel.VideoViewModel
import com.abdur.projectgetvideos.data.model.VideoDetail
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abdur.projectgetvideos.common.Response
import com.abdur.projectgetvideos.common.Screen
import com.commandiron.compose_loading.CubeGrid

@Composable
fun VideoListItem(video: VideoDetail, onVideoClick: (Uri) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onVideoClick(video.uri) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = video.thumbnailBitmap,
            contentDescription = "thumbnail image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp),
            placeholder = painterResource(R.drawable.person_preview),
            error = painterResource(R.drawable.personal_injury_preview),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = video.title, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun VideoList(videos: List<VideoDetail>?, onVideoClick: (Uri) -> Unit) {
        LazyColumn {
            items(videos!!) { video ->
                VideoListItem(video, onVideoClick)
            }
        }
}


@Composable
fun VideoScreen(videoViewModel: VideoViewModel = viewModel()) {
    val response by videoViewModel.videos.observeAsState(Response.Loading())
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.VideoScreen.route) {
        composable(Screen.VideoScreen.route) {
            when (response) {
                is Response.Loading -> {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()){
                        CubeGrid()
                    }
                }
                is Response.Success -> {
                    val videos = (response as Response.Success<List<VideoDetail>>).data
                    VideoList(videos) { videoUri ->
                        val encodedUri = Uri.encode(videoUri.toString())
                        navController.navigate(Screen.VideoPlayer.route.replace("{videoUri}", encodedUri))
                    }
                }
            }
        }

        composable(
            Screen.VideoPlayer.route, arguments = listOf(
            navArgument("videoUri") {
                type = NavType.StringType
            }
        )) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("videoUri") ?: ""
            val videoUri = Uri.decode(encodedUri)
            VideoPlayer(Uri.parse(videoUri))
        }
    }
}