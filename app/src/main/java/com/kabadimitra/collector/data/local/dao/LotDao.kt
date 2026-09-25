package com.kabadimitra.collector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kabadimitra.collector.data.local.entities.Lot
import com.kabadimitra.collector.data.local.entities.LotStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLot(lot: Lot): Long

    @Update
    fun updateLot(lot: Lot): Int

    @Query("SELECT * FROM lots WHERE id = :lotId LIMIT 1")
    fun getLotById(lotId: String): Lot?

    @Query("SELECT * FROM lots WHERE id = :lotId LIMIT 1")
    fun observeLotById(lotId: String): Flow<Lot?>

    @Query("SELECT * FROM lots ORDER BY createdAt DESC")
    fun observeAllLots(): Flow<List<Lot>>

    @Query("UPDATE lots SET status = :status WHERE id = :lotId")
    fun updateStatus(lotId: String, status: LotStatus): Int

    @Query("SELECT * FROM lots WHERE status = :status ORDER BY createdAt DESC")
    fun observeLotsByStatus(status: LotStatus): Flow<List<Lot>>
}
