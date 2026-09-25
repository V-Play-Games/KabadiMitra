package com.kabadimitra.collector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kabadimitra.collector.data.local.entities.CollectorProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateProfile(profile: CollectorProfile): Long

    @Query("SELECT * FROM collector_profile LIMIT 1")
    fun observeProfile(): Flow<CollectorProfile?>

    @Query("SELECT * FROM collector_profile LIMIT 1")
    fun getProfile(): CollectorProfile?
}
