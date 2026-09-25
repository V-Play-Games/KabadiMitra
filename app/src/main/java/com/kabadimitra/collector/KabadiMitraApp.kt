package com.kabadimitra.collector

import android.app.Application
import com.kabadimitra.collector.data.di.AppContainer

class KabadiMitraApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
