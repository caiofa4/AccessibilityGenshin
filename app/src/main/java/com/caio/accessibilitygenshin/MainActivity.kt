package com.caio.accessibilitygenshin

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import com.caio.accessibilitygenshin.SharedData.shouldRun
import com.caio.accessibilitygenshin.ui.theme.AccessibilityGenshinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

//    MarkerAt91Percent()

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