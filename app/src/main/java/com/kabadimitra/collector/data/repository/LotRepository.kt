package com.kabadimitra.collector.data.repository

import com.kabadimitra.collector.data.local.dao.LotDao
import com.kabadimitra.collector.data.local.dao.OutboxDao
import com.kabadimitra.collector.data.local.entities.Lot
import com.kabadimitra.collector.data.local.entities.LotStatus
import com.kabadimitra.collector.data.local.entities.OutboxItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class LotRepository(
    private val lotDao: LotDao,
    private val outboxDao: OutboxDao
) {
    fun observeAllLots(): Flow<List<Lot>> = lotDao.observeAllLots()

    fun observeLotById(lotId: String): Flow<Lot?> = lotDao.observeLotById(lotId)

    suspend fun getLotById(lotId: String): Lot? = withContext(Dispatchers.IO) {
        lotDao.getLotById(lotId)
    }

    suspend fun createDraftLot(
        materialCategory: String,
        weightKg: Double,
        photoPath: String = "",
        classifierConfidence: Float? = 0.94f,
        estimateRupees: Int,
        pickupAddress: String = "धारावी 90 फीट रोड, मुंबई",
        pickupLat: Double = 19.0434,
        pickupLng: Double = 72.8562
    ): Lot = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("MMdd", Locale.getDefault())
        val randomSuffix = (1000..9999).random()
        val lotId = "KM-2026-${dateFormat.format(Date())}-$randomSuffix"

        val lot = Lot(
            id = lotId,
            collectorId = "COL-9821-4321",
            materialCategory = materialCategory,
            weightKg = weightKg,
            photoPath = photoPath,
            classifierConfidence = classifierConfidence,
            estimateRupees = estimateRupees,
            pickupLat = pickupLat,
            pickupLng = pickupLng,
            pickupAddress = pickupAddress,
            status = LotStatus.DRAFT,
            otpCode = "${(1000..9999).random()}"
        )

        lotDao.insertLot(lot)

        // Queue in Outbox for offline sync
        outboxDao.insertOutboxItem(
            OutboxItem(
                eventType = "CREATE_LOT",
                payloadJson = "{\"lotId\":\"$lotId\",\"weight\":$weightKg,\"material\":\"$materialCategory\"}"
            )
        )

        lot
    }

    suspend fun assignRecycler(lotId: String, recyclerId: String) = withContext(Dispatchers.IO) {
        val current = lotDao.getLotById(lotId)
        if (current != null) {
            val updated = current.copy(
                selectedRecyclerId = recyclerId,
                status = LotStatus.ACCEPTED
            )
            lotDao.updateLot(updated)
        }
    }

    suspend fun updateStatus(lotId: String, status: LotStatus) = withContext(Dispatchers.IO) {
        lotDao.updateStatus(lotId, status)
    }
}
