package com.kabadimitra.collector.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outbox")
data class OutboxItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventType: String, // "CREATE_LOT", "RECORD_TRANSACTION", "SIGNATURE_SYNC"
    val payloadJson: String,
    val retryCount: Int = 0,
    val status: String = "PENDING", // "PENDING", "SYNCED", "FAILED"
    val createdAt: Long = System.currentTimeMillis()
)
