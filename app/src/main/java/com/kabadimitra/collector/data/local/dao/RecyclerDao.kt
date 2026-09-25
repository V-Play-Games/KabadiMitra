package com.kabadimitra.collector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kabadimitra.collector.data.local.entities.RecyclerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecyclerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecyclers(recyclers: List<RecyclerEntity>): List<Long>

    @Query("SELECT * FROM recyclers ORDER BY (baseRateBonusPerKg + rating) DESC")
    fun observeAllRecyclers(): Flow<List<RecyclerEntity>>

    @Query("SELECT * FROM recyclers WHERE id = :id LIMIT 1")
    fun getRecyclerById(id: String): RecyclerEntity?

    @Query("SELECT COUNT(*) FROM recyclers")
    fun count(): Int
}
