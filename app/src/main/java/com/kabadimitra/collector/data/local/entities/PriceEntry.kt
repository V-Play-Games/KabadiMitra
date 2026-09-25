package com.kabadimitra.collector.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prices")
data class PriceEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val material: String, // "PET Plastic", "Cardboard", "Iron / Loha", "Copper", "Aluminium"
    val hindiName: String, // "पीईटी प्लास्टिक", "गत्ता / कागज़", "लोहा", "तांबा", "एल्युमिनियम"
    val category: String, // "Plastics", "Paper", "Metals"
    val area: String, // "Dharavi, Mumbai", "Kurla West", "Bandra"
    val ratePerKg: Int, // e.g. 18
    val previousRatePerKg: Int = ratePerKg, // e.g. 16
    val median14DaysRate: Int = ratePerKg,
    val jalaoLossPercentage: Int = 100, // Burning penalty warning
    val date: Long = System.currentTimeMillis()
) {
    val rateDiff: Int
        get() = ratePerKg - previousRatePerKg
}
