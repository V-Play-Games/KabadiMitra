package com.kabadimitra.collector.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collector_profile")
data class CollectorProfile(
    @PrimaryKey val id: String = "COL-9821-4321",
    val name: String = "रामू भाई (Ramu)",
    val phone: String = "+91 98210 54321",
    val language: String = "hi", // "hi", "mr", "en"
    val pin: String = "1234",
    val area: String = "Dharavi Sector 3, Mumbai",
    val kamaiScore: Int = 850,
    val monthlyEarningsRupees: Int = 14250,
    val earningsGrowthPercent: Int = 18,
    val isVerified: Boolean = true
)
