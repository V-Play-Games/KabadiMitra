package com.kabadimitra.collector.data.di

import android.content.Context
import com.kabadimitra.collector.core.camera.ImageCompressor
import com.kabadimitra.collector.core.crypto.RecordSigner
import com.kabadimitra.collector.core.ml.MaterialClassifier
import com.kabadimitra.collector.core.network.NetworkMonitor
import com.kabadimitra.collector.core.sync.SyncManager
import com.kabadimitra.collector.data.local.AppDatabase
import com.kabadimitra.collector.data.repository.CollectorRepository
import com.kabadimitra.collector.data.repository.LedgerRepository
import com.kabadimitra.collector.data.repository.LotRepository
import com.kabadimitra.collector.data.repository.PriceRepository
import com.kabadimitra.collector.data.repository.RecyclerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppContainer(private val context: Context) {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    val priceRepository: PriceRepository by lazy {
        PriceRepository(database.priceDao())
    }

    val lotRepository: LotRepository by lazy {
        LotRepository(database.lotDao(), database.outboxDao())
    }

    val recyclerRepository: RecyclerRepository by lazy {
        RecyclerRepository(database.recyclerDao())
    }

    val ledgerRepository: LedgerRepository by lazy {
        LedgerRepository(database.transactionDao(), database.outboxDao())
    }

    val collectorRepository: CollectorRepository by lazy {
        CollectorRepository(database.collectorDao())
    }

    val recordSigner: RecordSigner by lazy {
        RecordSigner()
    }

    val materialClassifier: MaterialClassifier by lazy {
        MaterialClassifier()
    }

    val imageCompressor: ImageCompressor by lazy {
        ImageCompressor()
    }

    val networkMonitor: NetworkMonitor by lazy {
        NetworkMonitor(context)
    }

    val syncManager: SyncManager by lazy {
        SyncManager(database.outboxDao())
    }

    init {
        // Ensure database fixture seed runs on background thread
        CoroutineScope(Dispatchers.IO).launch {
            if (database.priceDao().count() == 0) {
                database.seedInitialFixtures()
            }
        }
    }
}
