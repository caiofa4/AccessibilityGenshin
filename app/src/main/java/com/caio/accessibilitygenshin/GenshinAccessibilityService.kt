package com.caio.accessibilitygenshin

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.caio.accessibilitygenshin.SharedData.shouldRun
import com.caio.accessibilitygenshin.constants.Direction
import com.caio.accessibilitygenshin.constants.SwipeMode
import com.caio.accessibilitygenshin.constants.commandDelay
import com.caio.accessibilitygenshin.model.SwipeCoordinates
import com.caio.accessibilitygenshin.constants.packageId
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class GenshinAccessibilityService : AccessibilityService() {
    private val tag = "TESTTAG"

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d(tag, "\n********************************************************************")
        Log.d(tag, "onAccessibilityEvent")
        Log.d(tag, "rootInActiveWindow is null: ${rootInActiveWindow == null}")
        Log.d(tag, "packageName: ${event.packageName}")

        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow

        if (event.packageName == packageId) {
            rootNode?.let {
                if (shouldRun) {
                    Log.d(tag, "shouldRun")
                    runBlocking {
                        Log.d(tag, "runBlocking")
                        runGameCommands()
                    }
                    shouldRun = false
                }
            }
        }
    }

    private suspend fun runGameCommands() {
        Log.d(tag, "before runGameCommands")
        delay(commandDelay)
        moveRight()
        delay(commandDelay)
        moveLeft()
        delay(commandDelay)
        moveUp()
        delay(commandDelay)
        moveDown()
        delay(commandDelay)
        cameraUp()
        delay(commandDelay)
        cameraDown()
        delay(commandDelay)
        cameraRight()
        delay(commandDelay)
        cameraLeft()
        delay(commandDelay)
        pressJump()
        delay(commandDelay)
        pressAttack()
        delay(commandDelay)
        pressDash()
        delay(2 * commandDelay)
        launchPoCApp()
    }

    override fun onInterrupt() {
        Log.d(tag, "Service interrupted")
    }

    override fun onServiceConnected() {
        Log.d(tag, "Service connected")
        Log.d(tag, "canRetrieveWindowContent = ${serviceInfo.canRetrieveWindowContent}")
    }

    private fun getStartY(height: Int): Float {
        val ratioY = if (SharedData.device.isOpenFoldable()) {
            0.845f
        } else {
            0.763f
        }

        val centerY = height * ratioY
        return centerY.coerceIn(0f, height.toFloat())
    }

    private fun getStartX(mode: SwipeMode, width: Int): Float {
        val ratioX = if (SharedData.device.isOpenFoldable()) {
            0.845f
        } else {
            0.183f
        }

        val centerX = when (mode) {
            SwipeMode.Move -> width * ratioX            // left 20%
            SwipeMode.ChangeCamera -> width * 0.5f      // right 50%
        }.coerceIn(0f, width.toFloat())

        return centerX
    }

    private fun getSwipeCoordinates(
        direction: Direction,
        mode: SwipeMode,
        width: Int = SharedData.device.width,
        height: Int = SharedData.device.height
    ): SwipeCoordinates {
        val offset = 300f // how far to swipe

        val startX = getStartX(mode, width)
        val startY = getStartY(height)

        val (endX, endY) = when (direction) {
            Direction.Up -> Pair(startX, (startY - offset).coerceIn(0f, height.toFloat()))
            Direction.Down -> Pair(startX, (startY + offset).coerceIn(0f, height.toFloat()))
            Direction.Left -> Pair((startX - offset).coerceIn(0f, width.toFloat()), startY)
            Direction.Right -> Pair((startX + offset).coerceIn(0f, width.toFloat()), startY)
        }

        return SwipeCoordinates(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY
        )
    }

    private fun moveRight() {
        Log.d(tag, "moveRight")
        val coordinates = getSwipeCoordinates(Direction.Right, SwipeMode.Move)
        swipe(coordinates)
    }

    private fun moveLeft() {
        Log.d(tag, "moveLeft")
        val coordinates = getSwipeCoordinates(Direction.Left, SwipeMode.Move)
        swipe(coordinates)
    }

    private fun moveUp() {
        Log.d(tag, "moveUp")
        val coordinates = getSwipeCoordinates(Direction.Up, SwipeMode.Move)
        swipe(coordinates)
    }

    private fun moveDown() {
        Log.d(tag, "moveDown")
        val coordinates = getSwipeCoordinates(Direction.Down, SwipeMode.Move)
        swipe(coordinates)
    }

    private fun cameraRight() {
        Log.d(tag, "cameraRight")
        val coordinates = getSwipeCoordinates(Direction.Right, SwipeMode.ChangeCamera)
        swipe(coordinates)
    }

    private fun cameraLeft() {
        Log.d(tag, "cameraLeft")
        val coordinates = getSwipeCoordinates(Direction.Left, SwipeMode.ChangeCamera)
        swipe(coordinates)
    }

    private fun cameraUp() {
        Log.d(tag, "cameraUp")
        val coordinates = getSwipeCoordinates(Direction.Up, SwipeMode.ChangeCamera)
        swipe(coordinates)
    }

    private fun cameraDown() {
        Log.d(tag, "cameraDown")
        val coordinates = getSwipeCoordinates(Direction.Down, SwipeMode.ChangeCamera)
        swipe(coordinates)
    }

    private fun pressJump() {
        Log.d(tag, "pressJump")

        val (ratioX, ratioY) = if (SharedData.device.isOpenFoldable()) {
            0.921f to 0.77f // 92.1% of screen width and 77% of screen height
        } else {
            0.882f to 0.658f // 88.2% of screen width and 65.8% of screen height
        }

        val jumpX = SharedData.device.width * ratioX
        val jumpY = SharedData.device.height * ratioY
        Log.d(tag, "jumpX: $jumpX")
        Log.d(tag, "jumpY: $jumpY")
        pressAt(jumpX, jumpY)
    }

    private fun pressAttack() {
        Log.d(tag, "pressAttack")

        val (ratioX, ratioY) = if (SharedData.device.isOpenFoldable()) {
            0.827f to 0.847f // 82.7% of screen width and 84.7% of screen height
        } else {
            0.806f to 0.758f // 80.6% of screen width and 75.8% of screen height
        }

        val attackX = SharedData.device.width * ratioX
        val attackY = SharedData.device.height * ratioY
        Log.d(tag, "attackX: $attackX")
        Log.d(tag, "attackY: $attackY")
        pressAt(attackX, attackY)
    }

    private fun pressDash() {
        Log.d(tag, "pressDash")

        val (ratioX, ratioY) = if (SharedData.device.isOpenFoldable()) {
            0.921f to 0.909f
        } else {
            0.882f to 0.858f // 88.2% of screen width and 85.8% of screen height
        }

        val dashX = SharedData.device.width * ratioX
        val dashY = SharedData.device.height * ratioY
        Log.d(tag, "dashX: $dashX")
        Log.d(tag, "jumpY: $dashY")
        pressAt(dashX, dashY)
    }

    private fun swipe(coordinates: SwipeCoordinates) {
        val path = Path().apply {
            Log.d(tag, "start x: ${coordinates.startX}")
            Log.d(tag, "end x: ${coordinates.endX}")
            Log.d(tag, "start y: ${coordinates.startY}")
            Log.d(tag, "end y: ${coordinates.endY}")
            moveTo(coordinates.startX, coordinates.startY)
            lineTo(coordinates.endX, coordinates.endY)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 300))
            .build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                Log.d("Gesture", "Swipe completed")
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
                Log.d("Gesture", "Gesture cancelled")
            }
        }, null)
    }

    private fun pressAt(x: Float, y: Float, duration: Long = 50L) {
        val path = Path().apply {
            moveTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                Log.d("Gesture", "Press at ($x, $y) completed")
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
                Log.d("Gesture", "Press at ($x, $y) cancelled")
            }
        }, null)
    }

    private fun launchPoCApp() {
        Log.d(tag, "launchPoCApp")
        val launchIntent = packageManager.getLaunchIntentForPackage("com.caio.accessibilitygenshin")
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)
    }
}