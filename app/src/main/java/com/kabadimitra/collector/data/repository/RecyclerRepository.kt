package com.kabadimitra.collector.data.repository

import com.kabadimitra.collector.data.local.dao.RecyclerDao
import com.kabadimitra.collector.data.local.entities.RecyclerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

data class MatchedRecycler(
    val entity: RecyclerEntity,
    val calculatedPayoutRupees: Int,
    val isBestOffer: Boolean
)

class RecyclerRepository(
    private val recyclerDao: RecyclerDao
) {
    fun observeAllRecyclers(): Flow<List<RecyclerEntity>> = recyclerDao.observeAllRecyclers()

    suspend fun getRecyclerById(id: String): RecyclerEntity? = withContext(Dispatchers.IO) {
        recyclerDao.getRecyclerById(id)
    }

    suspend fun matchRecyclers(weightKg: Double, baseRatePerKg: Int): List<MatchedRecycler> = withContext(Dispatchers.IO) {
        // Collect current recyclers
        val list = mutableListOf<RecyclerEntity>()
        recyclerDao.getRecyclerById("REC-MUM-01")?.let { list.add(it) }
        recyclerDao.getRecyclerById("REC-MUM-02")?.let { list.add(it) }
        recyclerDao.getRecyclerById("REC-MUM-03")?.let { list.add(it) }

        val bestBonus = list.maxOfOrNull { it.baseRateBonusPerKg } ?: 0

        list.map { recycler ->
            val totalRate = baseRatePerKg + recycler.baseRateBonusPerKg
            val totalPayout = (weightKg * totalRate).toInt()
            MatchedRecycler(
                entity = recycler,
                calculatedPayoutRupees = totalPayout,
                isBestOffer = recycler.baseRateBonusPerKg == bestBonus
            )
        }.sortedByDescending { it.calculatedPayoutRupees }
    }
}
