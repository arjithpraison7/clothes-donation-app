package com.arjithpraison.clothesdonationapp.ui.theme

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.arjithpraison.clothesdonationapp.R


@Composable
fun SplashScreen(navController: NavHostController) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.app_logo_gif))
                setOnCompletionListener {
                    // Navigate to the main screen once the video finishes
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true } // Remove the splash screen from the back stack
                    }
                }
                start() // Start playing the video
            }
        }
    )
}