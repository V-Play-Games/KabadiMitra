package com.kabadimitra.collector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kabadimitra.collector.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactions WHERE lotId = :lotId LIMIT 1")
    fun getTransactionByLotId(lotId: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE lotId = :lotId LIMIT 1")
    fun observeTransactionByLotId(lotId: String): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions ORDER BY handoverTimestamp DESC")
    fun observeAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(totalAmountRupees) FROM transactions")
    fun observeTotalEarnings(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM transactions")
    fun count(): Int
}
