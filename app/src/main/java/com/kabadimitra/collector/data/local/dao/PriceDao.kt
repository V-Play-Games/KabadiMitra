package com.kabadimitra.collector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kabadimitra.collector.data.local.entities.PriceEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrices(prices: List<PriceEntry>): List<Long>

    @Query("SELECT * FROM prices ORDER BY category ASC, material ASC")
    fun observeAllPrices(): Flow<List<PriceEntry>>

    @Query("SELECT * FROM prices WHERE category = :category ORDER BY material ASC")
    fun observePricesByCategory(category: String): Flow<List<PriceEntry>>

    @Query("SELECT * FROM prices WHERE material = :material LIMIT 1")
    fun getPriceForMaterial(material: String): PriceEntry?

    @Query("SELECT COUNT(*) FROM prices")
    fun count(): Int
}
