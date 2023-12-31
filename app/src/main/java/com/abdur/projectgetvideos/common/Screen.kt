package com.abdur.projectgetvideos.common

sealed class Screen(val route: String) {
    object VideoScreen : Screen("video_screen")
    object VideoPlayer : Screen("video_player/{videoUri}")
}

