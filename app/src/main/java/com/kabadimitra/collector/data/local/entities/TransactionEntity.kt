package com.kabadimitra.collector.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val lotId: String,
    val recyclerId: String,
    val recyclerName: String,
    val materialCategory: String,
    val finalWeightKg: Double,
    val finalRatePerKg: Int,
    val totalAmountRupees: Int,
    val paymentMethod: String, // "CASH", "UPI"
    val collectorSignature: String,
    val recyclerSignature: String?,
    val gpsMatchMeters: Int?,
    val isPaid: Boolean = true,
    val handoverTimestamp: Long = System.currentTimeMillis()
)
