package com.kabadimitra.collector.data.repository

import com.kabadimitra.collector.data.local.dao.CollectorDao
import com.kabadimitra.collector.data.local.entities.CollectorProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CollectorRepository(
    private val collectorDao: CollectorDao
) {
    fun observeProfile(): Flow<CollectorProfile?> = collectorDao.observeProfile()

    suspend fun getProfile(): CollectorProfile? = withContext(Dispatchers.IO) {
        collectorDao.getProfile()
    }

    suspend fun updateLanguage(langCode: String) = withContext(Dispatchers.IO) {
        val profile = collectorDao.getProfile()
        if (profile != null) {
            collectorDao.insertOrUpdateProfile(profile.copy(language = langCode))
        }
    }

    suspend fun updatePin(newPin: String) = withContext(Dispatchers.IO) {
        val profile = collectorDao.getProfile()
        if (profile != null) {
            collectorDao.insertOrUpdateProfile(profile.copy(pin = newPin))
        }
    }
}
