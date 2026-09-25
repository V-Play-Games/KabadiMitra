package com.kabadimitra.collector.core.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioClipPlayer(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AudioClipPlayer", "Failed to init TextToSpeech", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Prefer Hindi locale for Devanagari prompts
            val result = tts?.setLanguage(Locale("hi", "IN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(0.92f) // Slightly slower rate for clear accessibility
        }
    }

    fun speak(text: String) {
        if (tts != null && isInitialized) {
            _isPlaying.value = true
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "km_audio_${System.currentTimeMillis()}")
            // Reset playing state after brief delay
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _isPlaying.value = false
            }, 3000)
        }
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
