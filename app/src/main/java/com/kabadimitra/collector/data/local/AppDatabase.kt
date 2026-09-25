package com.kabadimitra.collector.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kabadimitra.collector.data.local.dao.CollectorDao
import com.kabadimitra.collector.data.local.dao.LotDao
import com.kabadimitra.collector.data.local.dao.OutboxDao
import com.kabadimitra.collector.data.local.dao.PriceDao
import com.kabadimitra.collector.data.local.dao.RecyclerDao
import com.kabadimitra.collector.data.local.dao.TransactionDao
import com.kabadimitra.collector.data.local.entities.CollectorProfile
import com.kabadimitra.collector.data.local.entities.Lot
import com.kabadimitra.collector.data.local.entities.LotStatus
import com.kabadimitra.collector.data.local.entities.OutboxItem
import com.kabadimitra.collector.data.local.entities.PriceEntry
import com.kabadimitra.collector.data.local.entities.RecyclerEntity
import com.kabadimitra.collector.data.local.entities.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Lot::class,
        TransactionEntity::class,
        PriceEntry::class,
        RecyclerEntity::class,
        CollectorProfile::class,
        OutboxItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lotDao(): LotDao
    abstract fun transactionDao(): TransactionDao
    abstract fun priceDao(): PriceDao
    abstract fun recyclerDao(): RecyclerDao
    abstract fun collectorDao(): CollectorDao
    abstract fun outboxDao(): OutboxDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kabadimitra_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Seed default fixture data on initial creation
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.seedInitialFixtures()
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun seedInitialFixtures() {
        // Seed default collector profile
        collectorDao().insertOrUpdateProfile(
            CollectorProfile(
                id = "COL-9821-4321",
                name = "रामू भाई (Ramu)",
                phone = "+91 98210 54321",
                language = "hi",
                pin = "1234",
                area = "धारावी सेक्टर 3, मुंबई",
                kamaiScore = 850,
                monthlyEarningsRupees = 14250,
                earningsGrowthPercent = 18,
                isVerified = true
            )
        )

        // Seed Bhav rates
        val prices = listOf(
            PriceEntry(
                material = "PET Plastic",
                hindiName = "पीईटी प्लास्टिक बोतलें",
                category = "Plastics",
                area = "धारावी मंडी, मुंबई",
                ratePerKg = 18,
                previousRatePerKg = 16,
                median14DaysRate = 17,
                jalaoLossPercentage = 100
            ),
            PriceEntry(
                material = "Cardboard / गत्ता",
                hindiName = "गत्ता और रद्दी पुट्ठा",
                category = "Paper",
                area = "धारावी मंडी, मुंबई",
                ratePerKg = 11,
                previousRatePerKg = 10,
                median14DaysRate = 11,
                jalaoLossPercentage = 100
            ),
            PriceEntry(
                material = "Iron / Loha",
                hindiName = "कबाड़ लोहा (मशीनरी/सरिया)",
                category = "Metals",
                area = "कुर्ला स्क्रैप यार्ड",
                ratePerKg = 32,
                previousRatePerKg = 33,
                median14DaysRate = 32,
                jalaoLossPercentage = 100
            ),
            PriceEntry(
                material = "Copper Wire",
                hindiName = "शुद्ध तांबा वायर",
                category = "Metals",
                area = "कुर्ला स्क्रैप यार्ड",
                ratePerKg = 460,
                previousRatePerKg = 445,
                median14DaysRate = 450,
                jalaoLossPercentage = 100
            ),
            PriceEntry(
                material = "Aluminium",
                hindiName = "एल्युमिनियम केन और बर्तन",
                category = "Metals",
                area = "धारावी मंडी, मुंबई",
                ratePerKg = 115,
                previousRatePerKg = 115,
                median14DaysRate = 114,
                jalaoLossPercentage = 100
            ),
            PriceEntry(
                material = "HDPE Drums",
                hindiName = "एचडीपीई प्लास्टिक डिब्बे/ड्रम",
                category = "Plastics",
                area = "धारावी मंडी, मुंबई",
                ratePerKg = 28,
                previousRatePerKg = 26,
                median14DaysRate = 27,
                jalaoLossPercentage = 100
            )
        )
        priceDao().insertPrices(prices)

        // Seed Verified CPCB Recyclers
        val recyclers = listOf(
            RecyclerEntity(
                id = "REC-MUM-01",
                name = "EcoGreen Polymers (इको-ग्रीन पॉलीमर्स)",
                area = "कुर्ला इंडस्ट्रियल एस्टेट, 1.4 किमी",
                distanceKm = 1.4,
                cpcbCertified = true,
                rating = 4.9f,
                baseRateBonusPerKg = 2,
                pickupSlot = "आज दोपहर 3:00 - 5:00 PM",
                verifiedLotsCount = 420,
                contactPhone = "+91 98200 11223"
            ),
            RecyclerEntity(
                id = "REC-MUM-02",
                name = "Mumbai Paper Mills Aggregator",
                area = "बांद्रा पूर्व, 2.2 किमी",
                distanceKm = 2.2,
                cpcbCertified = true,
                rating = 4.8f,
                baseRateBonusPerKg = 1,
                pickupSlot = "आज शाम 5:30 PM",
                verifiedLotsCount = 280,
                contactPhone = "+91 98200 44556"
            ),
            RecyclerEntity(
                id = "REC-MUM-03",
                name = "Dharavi Circular Metals (धारावी मेटल्स)",
                area = "धारावी लेबर कैंप, 0.8 किमी",
                distanceKm = 0.8,
                cpcbCertified = true,
                rating = 4.7f,
                baseRateBonusPerKg = 3,
                pickupSlot = "तुरंत पिकअप उपलब्ध (30 मिनट)",
                verifiedLotsCount = 512,
                contactPhone = "+91 98200 77889"
            )
        )
        recyclerDao().insertRecyclers(recyclers)

        // Seed demo completed transactions for Hisaab
        val sampleTransactions = listOf(
            TransactionEntity(
                lotId = "KM-2026-0410",
                recyclerId = "REC-MUM-01",
                recyclerName = "EcoGreen Polymers",
                materialCategory = "PET Plastic",
                finalWeightKg = 40.0,
                finalRatePerKg = 20,
                totalAmountRupees = 800,
                paymentMethod = "UPI",
                collectorSignature = "ed25519_sig_col_0410",
                recyclerSignature = "ed25519_sig_rec_0410",
                gpsMatchMeters = 12,
                isPaid = true,
                handoverTimestamp = System.currentTimeMillis() - 86400000L * 2
            ),
            TransactionEntity(
                lotId = "KM-2026-0412",
                recyclerId = "REC-MUM-03",
                recyclerName = "Dharavi Circular Metals",
                materialCategory = "Iron / Loha",
                finalWeightKg = 52.0,
                finalRatePerKg = 35,
                totalAmountRupees = 1820,
                paymentMethod = "CASH",
                collectorSignature = "ed25519_sig_col_0412",
                recyclerSignature = "ed25519_sig_rec_0412",
                gpsMatchMeters = 8,
                isPaid = true,
                handoverTimestamp = System.currentTimeMillis() - 86400000L * 4
            ),
            TransactionEntity(
                lotId = "KM-2026-0415",
                recyclerId = "REC-MUM-02",
                recyclerName = "Mumbai Paper Mills",
                materialCategory = "Cardboard / गत्ता",
                finalWeightKg = 110.0,
                finalRatePerKg = 12,
                totalAmountRupees = 1320,
                paymentMethod = "UPI",
                collectorSignature = "ed25519_sig_col_0415",
                recyclerSignature = "ed25519_sig_rec_0415",
                gpsMatchMeters = 15,
                isPaid = true,
                handoverTimestamp = System.currentTimeMillis() - 86400000L * 6
            )
        )
        sampleTransactions.forEach { transactionDao().insertTransaction(it) }

        // Seed an active demo lot
        val demoLot = Lot(
            id = "KM-2026-0417",
            collectorId = "COL-9821-4321",
            materialCategory = "PET Plastic",
            weightKg = 28.5,
            photoPath = "lot_sample_pet.jpg",
            classifierConfidence = 0.94f,
            estimateRupees = 570,
            pickupLat = 19.0434,
            pickupLng = 72.8562,
            pickupAddress = "धारावी 90 फीट रोड, मुंबई",
            status = LotStatus.ACCEPTED,
            selectedRecyclerId = "REC-MUM-01",
            otpCode = "4812",
            createdAt = System.currentTimeMillis() - 3600000L
        )
        lotDao().insertLot(demoLot)
    }
}
