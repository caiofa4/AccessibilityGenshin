package com.caio.accessibilitygenshin

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.caio.accessibilitygenshin.SharedData.device
import com.caio.accessibilitygenshin.SharedData.shouldRun
import com.caio.accessibilitygenshin.model.Device
import com.caio.accessibilitygenshin.ui.theme.AccessibilityGenshinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tell the system that we will handle window insets manually
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            AccessibilityGenshinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

//        hideSystemUI()

        getDeviceInfo(this)
    }

    private fun getDeviceInfo(context: Context) {
//        var width = Resources.getSystem().displayMetrics.widthPixels
//        var height = Resources.getSystem().displayMetrics.heightPixels
//
//        Log.d("TESTTAG", "Width: $width, Height: $height")
//        if (height > width) {
//            height = width.also { width = height }
//        }
//
//        device = Device(height, width)
//        Log.d("TESTTAG", "Width: $width, Height: $height")
//

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val bounds = wm.currentWindowMetrics.bounds

        var fullWidth = bounds.width()
        var fullHeight = bounds.height()

        Log.d("TESTTAG", "Width: $fullWidth, Height: $fullHeight")
        if (fullHeight > fullWidth) {
            fullHeight = fullWidth.also { fullWidth = fullHeight }
        }

        device = Device(fullHeight, fullWidth)
        Log.d("TESTTAG", "Width: $fullWidth, Height: $fullHeight")
    }

    private fun hideSystemUI() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    MarkerAt91Percent()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Enable Accessibility Permission")
        }

        Button(
            onClick = {
                launchGenshinImpact()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Open Genshin Impact")
        }
    }
}

@Composable
fun MarkerAt91Percent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val markerX = size.width * 0.882f
            drawLine(
                color = Color.Red,
                start = Offset(markerX, 0f),
                end = Offset(markerX, size.height),
                strokeWidth = 4f
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val markerY = size.height * 0.76f
            drawLine(
                color = Color.Green,
                start = Offset(0f, markerY),
                end = Offset(size.width, markerY),
                strokeWidth = 4f
            )
        }
    }
}

fun launchGenshinImpact() {
    val context = MyApplication.instance
    val packageName = "com.miHoYo.GenshinImpact" // Global version
    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)

    if (launchIntent != null) {
        shouldRun = true
        context.startActivity(launchIntent)
    } else {
        Toast.makeText(context, "Genshin Impact is not installed.", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AccessibilityGenshinTheme {
        MainScreen()
    }
}