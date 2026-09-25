package com.kabadimitra.collector.data.di

import android.content.Context
import com.kabadimitra.collector.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppContainer(private val context: Context) {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    init {
        // Trigger initial fixture check/seed asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            if (database.priceDao().count() == 0) {
                database.seedInitialFixtures()
            }
        }
    }
}
