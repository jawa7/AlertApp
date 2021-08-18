package com.example.alertapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.Toast
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import kotlin.math.roundToInt

class OverlayShowingService : Service() {

//    private lateinit var topLeftView: View
//    private var offsetX: Float = 0.0f
//    private var offsetY: Float = 0.0f
//    private var originalXPos: Int = 0
//    private var originalYPos: Int = 0
//    private var moving: Boolean = false
    private lateinit var windowManager: WindowManager

    private lateinit var composeView: ComposeView

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager


        val params: WindowManager.LayoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT
            )
        params.gravity = Gravity.START or Gravity.TOP
        params.x = 0
        params.y = 0

        composeView = ComposeView(this)
        composeView.setContent {
//            var offsetX by remember { mutableStateOf(0f) }
//            var offsetY by remember { mutableStateOf(0f) }
            IconButton(
                onClick = { Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show() },
                modifier = Modifier
//                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                    .pointerInput(MainActivity()) {
//                        detectDragGestures { change, dragAmount ->
//                            change.consumeAllChanges()
//                            offsetX += dragAmount.x
//                            offsetY += dragAmount.y
//                        }
//                    }
                    .padding(16.dp)
                    .size(24.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "",
                    modifier = Modifier,
                    tint = androidx.compose.ui.graphics.Color.Green
                )
            }
        }

        val lifecycleOwner = MyLifecycleOwner()
        val viewModelStore = ViewModelStore()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        ViewTreeLifecycleOwner.set(composeView, lifecycleOwner)
        ViewTreeViewModelStoreOwner.set(composeView)
        { viewModelStore }
        ViewTreeSavedStateRegistryOwner.set(composeView, lifecycleOwner)

        windowManager.addView(composeView, params)
    }


//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        if (event?.action == MotionEvent.ACTION_DOWN) {
//            val x: Float = event.rawX
//            val y: Float = event.rawY
//
//            moving = false
//
//            val location: MutableList<Int> = mutableListOf()
//
//            originalXPos = location[0]
//            originalYPos = location[1]
//
//            offsetX = originalXPos - x
//            offsetY = originalYPos - y
//        } else if (event?.action == MotionEvent.ACTION_MOVE) {
//            val topLeftLocationOnScreen = IntArray(2)
//            topLeftView.getLocationOnScreen(topLeftLocationOnScreen)
//
//            println("topLeftLocation = " + topLeftLocationOnScreen[1])
//            println("originalY = $originalYPos")
//
//            val x: Float = event.rawX
//            val y: Float = event.rawY
//
//            //val params = composeView.layoutParams as WindowManager.LayoutParams
//
//            val newX: Int = (offsetX + x).roundToInt()
//            val newY: Int = (offsetY + y).roundToInt()
//
//            if (Math.abs(newX - originalXPos) < 1 && abs(newY - originalYPos) < 1 && !moving) {
//                return false
//            }
//
//            // params.x = newX - (topLeftLocationOnScreen[0])
//            // params.y = newY - (topLeftLocationOnScreen[1])
//
//            // windowManager.updateViewLayout(composeView, params)
//            moving = true
//        } else if (event?.action == MotionEvent.ACTION_UP) {
//            if (moving) {
//                return true
//            }
//        }
//        return false
//    }

//    override fun onClick(v: View?) {
//        Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show()
//    }
}