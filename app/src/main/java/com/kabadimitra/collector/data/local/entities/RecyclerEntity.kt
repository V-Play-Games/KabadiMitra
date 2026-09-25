package com.kabadimitra.collector.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recyclers")
data class RecyclerEntity(
    @PrimaryKey val id: String, // "REC-MUM-01"
    val name: String, // "EcoGreen Plastics Pvt Ltd"
    val area: String, // "Kurla Industrial Estate"
    val distanceKm: Double, // 1.8
    val cpcbCertified: Boolean, // true
    val rating: Float, // 4.8
    val baseRateBonusPerKg: Int, // +2
    val pickupSlot: String, // "आज 3:00 - 5:00 PM"
    val verifiedLotsCount: Int, // 340
    val contactPhone: String // "+91 98765 43210"
)
