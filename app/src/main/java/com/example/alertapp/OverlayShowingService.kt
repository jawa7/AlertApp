package com.example.alertapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.view.*
import android.view.View.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner

class OverlayShowingService : Service() {

    private lateinit var windowManager: WindowManager

    private lateinit var playButton: ComposeView

    private lateinit var stopButton: ComposeView


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Initializing Compose
        playButton = ComposeView(this)

        stopButton = ComposeView(this)

        // Writing parameters for buttons for View system
        val params: WindowManager.LayoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
            )
        params.gravity = Gravity.START or Gravity.TOP
        params.x = 0
        params.y = 0

        // Creating ringtone manager and media player for the signal to sound
        val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mMediaPlayer = MediaPlayer()
        mMediaPlayer.setDataSource(this, alert)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0 && !mMediaPlayer.isPlaying) {
            mMediaPlayer.setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            // isLooping true for loop the sound
            mMediaPlayer.isLooping = true
        }

        // We create both buttons, but we want to display only at the start - playButton
        stopButton.visibility = GONE

        playButton.setContent {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "",
                modifier = Modifier
                    .combinedClickable(
                    onClick = {
                        // When clicking on playButton starts the sound and playButton changes to stopButton
                        mMediaPlayer.prepare()
                        mMediaPlayer.start()
                        playButton.visibility = GONE
                        stopButton.visibility = VISIBLE
                    },
                    onLongClick = {
                        // If it is long press playButton and stopButton will be deleted
                        windowManager.removeViewImmediate(playButton)
                        windowManager.removeViewImmediate(stopButton)
                    }
                )
                    .padding(12.dp),
                tint = Color.Green
            )
        }


        stopButton.setContent {
            IconButton(
                onClick = {
                    mMediaPlayer.stop()
                    stopButton.visibility = GONE
                    playButton.visibility = VISIBLE
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier
                )
            }
        }
        // Initializing lifecycle for Compose
        lifeCycleCompose(playButton)
        lifeCycleCompose(stopButton)

        windowManager.addView(stopButton, params)
        windowManager.addView(playButton, params)
    }

    private fun lifeCycleCompose(v: View) {
        //Trick the ComposeView into thinking we are tracking lifecycle
        val lifecycleOwner = MyLifecycleOwner()
        val viewModelStore = ViewModelStore()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        ViewTreeLifecycleOwner.set(v, lifecycleOwner)
        ViewTreeViewModelStoreOwner.set(v)
        { viewModelStore }
        ViewTreeSavedStateRegistryOwner.set(v, lifecycleOwner)
    }
}