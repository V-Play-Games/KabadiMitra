package com.kabadimitra.collector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kabadimitra.collector.data.local.entities.OutboxItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutboxItem(item: OutboxItem): Long

    @Query("SELECT * FROM outbox WHERE status = 'PENDING' ORDER BY createdAt ASC")
    fun getPendingItems(): List<OutboxItem>

    @Query("SELECT * FROM outbox ORDER BY createdAt DESC")
    fun observeAllItems(): Flow<List<OutboxItem>>

    @Query("UPDATE outbox SET status = :status, retryCount = retryCount + 1 WHERE id = :id")
    fun updateStatus(id: Long, status: String): Int

    @Query("DELETE FROM outbox WHERE status = 'SYNCED'")
    fun clearSynced(): Int
}
