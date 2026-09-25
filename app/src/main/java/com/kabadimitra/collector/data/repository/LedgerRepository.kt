package com.kabadimitra.collector.data.repository

import com.kabadimitra.collector.data.local.dao.OutboxDao
import com.kabadimitra.collector.data.local.dao.TransactionDao
import com.kabadimitra.collector.data.local.entities.OutboxItem
import com.kabadimitra.collector.data.local.entities.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LedgerRepository(
    private val transactionDao: TransactionDao,
    private val outboxDao: OutboxDao
) {
    fun observeAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.observeAllTransactions()

    fun observeTransactionByLotId(lotId: String): Flow<TransactionEntity?> = transactionDao.observeTransactionByLotId(lotId)

    fun observeTotalEarnings(): Flow<Int?> = transactionDao.observeTotalEarnings()

    suspend fun recordVerifiedTransaction(
        lotId: String,
        recyclerId: String,
        recyclerName: String,
        materialCategory: String,
        finalWeightKg: Double,
        finalRatePerKg: Int,
        totalAmountRupees: Int,
        paymentMethod: String,
        collectorSignature: String,
        recyclerSignature: String?,
        gpsMatchMeters: Int? = 12
    ): TransactionEntity = withContext(Dispatchers.IO) {
        val transaction = TransactionEntity(
            lotId = lotId,
            recyclerId = recyclerId,
            recyclerName = recyclerName,
            materialCategory = materialCategory,
            finalWeightKg = finalWeightKg,
            finalRatePerKg = finalRatePerKg,
            totalAmountRupees = totalAmountRupees,
            paymentMethod = paymentMethod,
            collectorSignature = collectorSignature,
            recyclerSignature = recyclerSignature,
            gpsMatchMeters = gpsMatchMeters,
            isPaid = true
        )

        transactionDao.insertTransaction(transaction)

        // Add to outbox for offline sync to the FastAPI backend
        outboxDao.insertOutboxItem(
            OutboxItem(
                eventType = "RECORD_TRANSACTION",
                payloadJson = "{\"lotId\":\"$lotId\",\"amount\":$totalAmountRupees,\"sig\":\"$collectorSignature\"}"
            )
        )

        transaction
    }
}
