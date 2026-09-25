package com.kabadimitra.collector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kabadimitra.collector.core.audio.AudioClipPlayer
import com.kabadimitra.collector.core.designsystem.KabadiMitraTheme
import com.kabadimitra.collector.ui.navigation.KmNavHost

class MainActivity : ComponentActivity() {

    private lateinit var audioPlayer: AudioClipPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as KabadiMitraApp
        audioPlayer = AudioClipPlayer(this)

        setContent {
            KabadiMitraTheme {
                KmNavHost(
                    container = app.container,
                    audioPlayer = audioPlayer
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.release()
    }
}
