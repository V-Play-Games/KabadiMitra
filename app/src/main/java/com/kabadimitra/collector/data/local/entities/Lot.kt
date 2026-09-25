package com.kabadimitra.collector.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lots")
data class Lot(
    @PrimaryKey val id: String, // e.g. "KM-2026-0417"
    val collectorId: String,
    val materialCategory: String, // e.g. "PET Bottles", "Cardboard / गत्ता", "Aluminium Cans"
    val weightKg: Double,
    val photoPath: String,
    val classifierConfidence: Float?,
    val estimateRupees: Int,
    val pickupLat: Double,
    val pickupLng: Double,
    val pickupAddress: String,
    val status: LotStatus,
    val selectedRecyclerId: String? = null,
    val otpCode: String = "4812",
    val createdAt: Long = System.currentTimeMillis()
)
