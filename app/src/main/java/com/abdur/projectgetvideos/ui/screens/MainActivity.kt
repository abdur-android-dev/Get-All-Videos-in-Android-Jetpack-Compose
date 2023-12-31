package com.abdur.projectgetvideos.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.abdur.projectgetvideos.ui.theme.ProjectGetVideosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
        private  var STORAGE_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES
    }
    private val permissionState =  mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            STORAGE_PERMISSION = Manifest.permission.READ_MEDIA_VIDEO
        }else{
            STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        }
        setContent {
            ProjectGetVideosTheme {
                checkPermission(permissionState)
                if (permissionState.value){
                    VideoScreen()
                }
            }
        }
    }

    private fun checkPermission(isPermissionGranted: MutableState<Boolean>){
        if(ContextCompat.checkSelfPermission(this, STORAGE_PERMISSION)==PackageManager.PERMISSION_GRANTED){
            isPermissionGranted.value = true
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(STORAGE_PERMISSION), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionState.value = true
                }
            }
        }
    }
}