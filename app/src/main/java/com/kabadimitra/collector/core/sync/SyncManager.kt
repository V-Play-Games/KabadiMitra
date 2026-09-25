package com.kabadimitra.collector.core.sync

import com.kabadimitra.collector.data.local.dao.OutboxDao
import com.kabadimitra.collector.data.local.entities.OutboxItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class SyncResult(
    val processedCount: Int,
    val successCount: Int,
    val failedCount: Int
)

class SyncManager(
    private val outboxDao: OutboxDao
) {
    suspend fun syncPendingOutbox(): SyncResult = withContext(Dispatchers.IO) {
        val pending = outboxDao.getPendingItems()
        var success = 0
        var failed = 0

        for (item in pending) {
            try {
                // Simulate HTTP POST payload to FastAPI backend
                // When successful:
                outboxDao.updateStatus(item.id, "SYNCED")
                success++
            } catch (e: Exception) {
                outboxDao.updateStatus(item.id, "FAILED")
                failed++
            }
        }

        // Clean up already synced records
        outboxDao.clearSynced()

        SyncResult(
            processedCount = pending.size,
            successCount = success,
            failedCount = failed
        )
    }
}
