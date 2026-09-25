package com.kabadimitra.collector.data.repository

import com.kabadimitra.collector.data.local.dao.PriceDao
import com.kabadimitra.collector.data.local.entities.PriceEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

data class EstimateResult(
    val material: String,
    val hindiName: String,
    val weightKg: Double,
    val ratePerKg: Int,
    val median14DaysRate: Int,
    val totalEstimateRupees: Int,
    val jalaoLossPercentage: Int = 100,
    val cpcbBonusRupees: Int = 2
)

class PriceRepository(
    private val priceDao: PriceDao
) {
    fun observeTodayRates(): Flow<List<PriceEntry>> {
        return priceDao.observeAllPrices()
    }

    fun observeRatesByCategory(category: String): Flow<List<PriceEntry>> {
        return priceDao.observePricesByCategory(category)
    }

    suspend fun calculateEstimate(materialName: String, weightKg: Double): EstimateResult = withContext(Dispatchers.IO) {
        val entry = priceDao.getPriceForMaterial(materialName)
        val rate = entry?.ratePerKg ?: 18
        val median = entry?.median14DaysRate ?: rate
        val total = (weightKg * rate).toInt()

        EstimateResult(
            material = materialName,
            hindiName = entry?.hindiName ?: materialName,
            weightKg = weightKg,
            ratePerKg = rate,
            median14DaysRate = median,
            totalEstimateRupees = total,
            jalaoLossPercentage = 100,
            cpcbBonusRupees = 2
        )
    }

    suspend fun get14DayMedianRate(material: String): Int = withContext(Dispatchers.IO) {
        val entry = priceDao.getPriceForMaterial(material)
        entry?.median14DaysRate ?: entry?.ratePerKg ?: 18
    }
}
